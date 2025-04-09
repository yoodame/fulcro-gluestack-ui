(ns com.fulcrologic.rad.rendering.gluestack-ui.entity-picker
  "Entity picker components for reference fields in Fulcro RAD forms using GlueStack UI.
   Provides to-one and to-many relationship pickers with support for all RAD picker options."
  (:require
    [com.fulcrologic.rad.rendering.gluestack-ui.utils :refer [transit->str str->transit]]
    [com.fulcrologic.fulcro.algorithms.form-state :as fs]
    [com.fulcrologic.fulcro.algorithms.normalized-state :as fns]
    [com.fulcrologic.fulcro.algorithms.tempid :as tempid]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.raw.components :as rc]
    [com.fulcrologic.fulcro-i18n.i18n :refer [tr]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [com.fulcrologic.fulcro.react.hooks :as hooks]
    [com.fulcrologic.fulcro.ui-state-machines :as uism]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.attributes-options :as ao]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.rad.picker-options :as po]
    [com.fulcrologic.gluestack-ui.lucide-icons :refer [chevron-down-icon]]
    [com.fulcrologic.gluestack-ui.components.ui.text :refer [ui-text]]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.hstack :refer [ui-h-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.vstack :refer [ui-v-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.text :refer [ui-text]]
    [com.fulcrologic.gluestack-ui.components.ui.button :refer [ui-button ui-button-text ui-button-icon]]
    [com.fulcrologic.gluestack-ui.components.ui.select :refer [ui-select ui-select-trigger ui-select-input
                                                               ui-select-portal ui-select-backdrop ui-select-icon ui-select-item
                                                               ui-select-content ui-select-drag-indicator-wrapper ui-select-drag-indicator]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control :refer [ui-form-control ui-form-control-label
                                                                     ui-form-control-label-text
                                                                     ui-form-control-error
                                                                     ui-form-control-error-text
                                                                     ui-form-control-helper-text]]
    [com.fulcrologic.rad.rendering.gluestack-ui.modals :refer [ui-form-modal]]
    [taoensso.encore :as enc]
    [taoensso.timbre :as log]))

(defn- integrate-with-parent-form! [{:keys [state app]} {:keys [parent-registry-key parent-ident parent-relation-attribute ident]}]
  (when (and parent-ident parent-relation-attribute parent-registry-key ident)
    (let [ParentForm      (comp/registry-key->class parent-registry-key)
          parent-props    (fns/ui->props @state ParentForm parent-ident)
          parent-relation (ao/qualified-key parent-relation-attribute)
          many?           (= (ao/cardinality parent-relation-attribute) :many)]
      (if-not (tempid/tempid? (second ident))
        (do (fns/swap!-> state
              (update-in (conj parent-ident parent-relation)
                (if many? #((fnil conj []) % ident)
                          (constantly ident))))
            (po/load-options! app ParentForm parent-props parent-relation-attribute
              {:force-reload? true})
            (comp/transact! app [(fs/mark-complete! {:entity-ident parent-ident
                                                     :field        parent-relation})]))
        (log/warn "Saving the new value for" parent-relation "returned OK from the server yet"
          "the tempid in" ident "has not been remapped to a real one, indicating that the save failed")))))

(defn toggle-modal* [state {:keys [open? picker-id edit-id]}]
  (-> state
    (assoc-in [::id picker-id :ui/open?] open?)
    (assoc-in [::id picker-id :ui/edit-id] edit-id)))

(defmutation toggle-modal [params]
  (action [{:keys [state]}]
    (swap! state toggle-modal* params)))

(defmutation saved [{:keys [picker-id] :as params}]
  (action [{:keys [state] :as env}]
    (integrate-with-parent-form! env params)
    (swap! state toggle-modal* {:open? false :picker-id picker-id})))

(defmutation cancel [{:keys [picker-id] :as _params}]
  (action [{:keys [state]}]
    (swap! state toggle-modal* {:open? false :picker-id picker-id})))

(defmutation quick-add [{:keys [ident entity parent-registry-key parent-ident parent-relation-attribute] :as params}]
  (ok-action [{:keys [tempid->realid] :as env}]
    (let [[ident entity] (tempid/resolve-tempids [ident entity] tempid->realid)
          params (assoc params :ident ident :entity entity)]
      (if (-> ident second tempid/tempid?)
        (log/error "Quick add failed. Server may not have saved the data")
        (integrate-with-parent-form! env params))))
  (remote [env]
    (let [delta {ident (reduce-kv
                         (fn [m k v]
                           (if (= k (first ident))
                             m
                             (assoc m k {:after v})))
                         {}
                         entity)}]
      (-> env
        (m/returning (rc/nc (vec (keys entity))))
        (m/with-server-side-mutation `form/save-as-form)
        (m/with-params {::form/master-pk (first ident)
                        ::form/id        (second ident)
                        ::form/delta     delta})))))


;; To-One Picker Component
(defsc ToOnePicker [this {:keys [env attr]}]
  {:use-hooks? true}
  (let [{::form/keys [master-form form-instance]} env
        visible? (form/field-visible? form-instance attr)]
    (hooks/use-lifecycle (fn []
                           (let [{:keys [env attr]} (comp/props this)
                                 props      (comp/props form-instance)
                                 form-class (comp/react-type form-instance)]
                             (po/load-options! form-instance form-class props attr))))
    (when visible?
      (let [field-options      (fo/get-field-options (comp/component-options form-instance) attr)
            {::attr/keys [qualified-key required?]} attr
            target-id-key      (ao/target attr)
            {Form      ::po/form
             ::po/keys [quick-create allow-edit? allow-create? cache-key query-key]} (merge attr field-options)
            Form               (?! (some-> Form (rc/registry-key->class)) form-instance attr)
            props              (comp/props form-instance)
            cache-key          (or (?! cache-key (comp/react-type form-instance) props) query-key)
            cache-key          (or cache-key query-key (log/error "Ref field MUST have either a ::picker-options/cache-key or ::picker-options/query-key in attribute " qualified-key))
            options            (get-in props [::po/options-cache cache-key :options])
            value              [target-id-key (get-in props [qualified-key target-id-key])]
            field-label        (form/field-label env attr)
            read-only?         (or (form/read-only? master-form attr) (form/read-only? form-instance attr))
            omit-label?        (form/omit-label? form-instance attr)
            invalid?           (and (not read-only?) (form/invalid-attribute-value? env attr))
            validation-message (when invalid? (form/validation-error-message env attr))
            can-edit?          (?! allow-edit? form-instance qualified-key)
            can-create?        (if-some [v (?! allow-create? form-instance qualified-key)] v (boolean Form))
            mutable?           (and Form (or can-edit? can-create?))
            onSelect           (fn [v] (form/input-changed! env qualified-key v))

            ;; Find currently selected option text
            selected-option    (first (filter #(= value (:value %)) options))
            selected-text      (when selected-option (:text selected-option))

            ;; State machine for modal
            picker-id          (hooks/use-generated-id)
            [picker-component] (hooks/use-state (fn [] (rc/nc [:ui/open? :ui/edit-id]
                                                         {:initial-state (constantly {})
                                                          :ident         (fn [_ _] [::id picker-id])})))
            _                  (hooks/use-gc this [::id picker-id] #{})
            {:ui/keys [open? edit-id]} (hooks/use-component (comp/any->app this) picker-component {:initialize true})]
        (ui-form-control {:key (str qualified-key) :isInvalid invalid?}
          ;; Label
          (when-not omit-label?
            (ui-form-control-label {}
              (ui-form-control-label-text {}
                (str field-label))))

          ;; Interactive picker
          (if (not mutable?)
            ;; Simple dropdown with no edit/create options
            (ui-select {:onValueChange (fn [encoded-v]
                                         (let [v (str->transit encoded-v)]
                                           (onSelect v)))
                        :isDisabled    read-only?}
              (ui-select-trigger {:variant "outline" :size "md" :className "bg-background-0"}
                (ui-select-input {:className   "flex-1"
                                  :placeholder (tr "Select...")
                                  :value       (or selected-text "")})
                (ui-select-icon {:className "mr-3" :as chevron-down-icon}))
              (ui-select-portal {:snapPoints [30]}
                (ui-select-backdrop {})
                (ui-select-content {}
                  (ui-select-drag-indicator-wrapper {}
                    (ui-select-drag-indicator {}))
                  (map (fn [{:keys [value text]}]
                         (ui-select-item {:key   (#?(:cljs transit->str :clj identity) value)
                                          :value (#?(:cljs transit->str :clj identity) value)
                                          :label text}))
                    options))))

            ;; Dropdown with edit/create buttons
            (ui-h-stack {:space "xs" :width "100%"}
              (ui-box {:flex 1}
                (ui-select {:onValueChange (fn [encoded-v]
                                             (let [v (str->transit encoded-v)]
                                               (onSelect v)))}
                  :isDisabled read-only?
                  (ui-select-trigger {}
                    (ui-select-input {:placeholder (tr "Select...")}
                      :value (or selected-text ""))
                    (ui-select-icon {}))
                  (ui-select-portal {}
                    (ui-select-content {}
                      (map (fn [{:keys [value text]}]
                             (ui-select-item {:key (transit->str value)}
                               :value (transit->str value)
                               :label text))
                        options)))))

              ;; Create button
              (when can-create?
                (ui-button {:size    "xs"
                            :variant "outline"
                            :onPress #(comp/transact! this [(toggle-modal {:open? true, :picker-id picker-id, :edit-id (tempid/tempid)})])}
                  (ui-button-text {} "+")))

              ;; Edit button
              (when can-edit?
                (ui-button {:size       "xs"
                            :variant    "outline"
                            :isDisabled (not (second value))
                            :onPress    #(comp/transact! this [(toggle-modal {:open? true, :picker-id picker-id, :edit-id (some-> value second)})])}
                  (ui-button-text {} "✎")))))

          ;; Error message for fields with omitted labels
          (when (and invalid? omit-label?)
            (ui-form-control-error {}
              (ui-form-control-error-text {} validation-message)))

          ;; Modal form when open
          (when open?
            (ui-form-modal {:Form            Form
                            :save-mutation   saved
                            :save-params     {:picker-id                 picker-id
                                              :parent-ident              (comp/get-ident form-instance)
                                              :parent-registry-key       (comp/class->registry-key (comp/get-class form-instance))
                                              :parent-relation-attribute attr}
                            :cancel-mutation cancel
                            :cancel-params   {:picker-id picker-id}
                            :id              edit-id})))))))

(def ui-to-one-picker (comp/factory ToOnePicker {:keyfn (fn [{:keys [attr]}] (::attr/qualified-key attr))}))

(defn to-one-picker [env attribute]
  (ui-to-one-picker {:env env :attr attribute}))

;; To-Many Picker Component
(defsc ToManyPicker [this {:keys [env attr]}]
  {:use-hooks? true}
  (hooks/use-lifecycle (fn []
                         (let [{:keys [env attr]} (comp/props this)
                               form-instance (::form/form-instance env)
                               props         (comp/props form-instance)
                               form-class    (comp/react-type form-instance)]
                           (po/load-options! form-instance form-class props attr))))
  (let [{::form/keys [form-instance]} env
        visible? (form/field-visible? form-instance attr)]
    (when visible?
      (let [{::form/keys [attributes] :as form-options} (comp/component-options form-instance)
            field-options      (fo/get-field-options form-options attr)
            {::attr/keys [qualified-key]} attr
            target-id-key      (first (keep (fn [{k ::attr/qualified-key ::attr/keys [target]}]
                                              (when (= k qualified-key) target)) attributes))
            {:keys     [style]
             Form      ::po/form
             ::po/keys [quick-create allow-create? allow-edit? cache-key query-key]} field-options
            cache-key          (or (?! cache-key (comp/react-type form-instance) (comp/props form-instance)) query-key)
            cache-key          (or cache-key query-key (log/error "Ref field MUST have either a ::picker-options/cache-key or ::picker-options/query-key in attribute " qualified-key))
            props              (comp/props form-instance)
            options            (get-in props [::po/options-cache cache-key :options])
            can-create?        (and Form (if-some [v (?! allow-create? form-instance qualified-key)] v (boolean Form)))
            current-selection  (into #{}
                                 (keep (fn [entity]
                                         (when-let [id (get entity target-id-key)]
                                           [target-id-key id])))
                                 (get props qualified-key))
            field-label        (form/field-label env attr)
            invalid?           (form/invalid-attribute-value? env attr)
            read-only?         (form/read-only? form-instance attr)
            omit-label?        (form/omit-label? form-instance attr)
            validation-message (when invalid? (form/validation-error-message env attr))

            ;; State machine for modal
            picker-id          (hooks/use-generated-id)
            [picker-component] (hooks/use-state (fn [] (rc/nc [:ui/open? :ui/edit-id]
                                                         {:initial-state (constantly {})
                                                          :ident         (fn [_ _] [::id picker-id])})))
            _                  (hooks/use-gc this [::id picker-id] #{})
            {:ui/keys [open? edit-id]} (hooks/use-component (comp/any->app this) picker-component {:initialize true})]

        (ui-form-control {:key (str qualified-key) :isInvalid invalid?}
          ;; Label
          (when-not omit-label?
            (ui-form-control-label {}
              (ui-form-control-label-text {}
                (str field-label))))

          ;; Multiple selection box
          (ui-v-stack {:space "xs" :width "100%"}
            ;; If read-only, just show selected items as text
            (if read-only?
              (ui-v-stack {:space "xs" :padding "2"}
                (map (fn [{:keys [text value]}]
                       (when (contains? current-selection value)
                         (ui-text {:key value} text)))
                  options))

              ;; Otherwise show dropdown or checkbox list
              (if (= style :dropdown)
                ;; Multi-select dropdown style
                (ui-h-stack {:width "100%"}
                  (ui-box {:flex 1}
                    (comp/fragment
                      ;; Placeholder for the multi-select dropdown
                      ;; In a real implementation, this would be a proper multi-select component
                      ;; Here we're showing a stub that would need to be replaced with
                      ;; a proper multi-select implementation for GlueStack UI
                      (ui-text {} "Multi-select dropdown would go here")
                      ;; Selected items display
                      (ui-v-stack {:space "xs" :padding "2"}
                        (map (fn [{:keys [text value]}]
                               (when (contains? current-selection value)
                                 (ui-text {:key value} text)))
                          options))))

                  ;; "Add" button for creating new items
                  (when can-create?
                    (ui-button {:size    "xs"
                                :variant "outline"
                                :onPress #(comp/transact! this [(toggle-modal {:open? true, :picker-id picker-id, :edit-id (tempid/tempid)})])}
                      (ui-button-text {} "+"))))

                ;; Checkbox list style
                (ui-v-stack {:space "xs" :padding "2" :borderWidth "1" :borderColor "gray.200" :borderRadius "md"}
                  (map (fn [{:keys [text value]}]
                         (let [checked? (contains? current-selection value)]
                           (ui-h-stack {:key value :alignItems "center" :space "sm"}
                             ;; This would be a proper checkbox in a real implementation
                             ;; Here we're showing a stub checkbox using a button
                             (ui-button {:size    "xs"
                                         :variant (if checked? "solid" "outline")
                                         :onPress #(if-not checked?
                                                     (form/input-changed! env qualified-key (vec (conj current-selection value)))
                                                     (form/input-changed! env qualified-key (vec (disj current-selection value))))}
                               (ui-button-text {} (if checked? "✓" " ")))
                             (ui-text {} text))))
                    options)

                  ;; Add button at the bottom of the list
                  (when can-create?
                    (ui-h-stack {:justifyContent "flex-end" :padding "2"}
                      (ui-button {:size    "sm"
                                  :variant "outline"
                                  :onPress #(comp/transact! this [(toggle-modal {:open? true, :picker-id picker-id, :edit-id (tempid/tempid)})])}
                        (ui-button-text {} "Add New"))))))))

          ;; Error message for fields with omitted labels
          (when (and invalid? omit-label?)
            (ui-form-control-error {}
              (ui-form-control-error-text {} validation-message)))

          ;; Modal form when open
          (when open?
            (ui-form-modal {:Form            Form
                            :save-mutation   saved
                            :save-params     {:picker-id                 picker-id
                                              :parent-ident              (comp/get-ident form-instance)
                                              :parent-registry-key       (comp/class->registry-key (comp/get-class form-instance))
                                              :parent-relation-attribute attr}
                            :cancel-mutation cancel
                            :cancel-params   {:picker-id picker-id}
                            :id              edit-id})))))))

(def ui-to-many-picker
  (comp/factory ToManyPicker {:keyfn (fn [{:keys [attr]}] (::attr/qualified-key attr))}))

(defn to-many-picker [env attribute]
  (ui-to-many-picker {:env env :attr attribute}))
