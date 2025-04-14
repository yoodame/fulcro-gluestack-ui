(ns com.fulcrologic.rad.rendering.gluestack-ui.enumerated-field
  "GlueStack UI renderer for enumerated fields in Fulcro RAD forms.
   Renders enum and keyword values as select dropdowns and checkbox groups."
  (:require
    #?@(:cljs [[cognitect.transit :as transit]
               [com.fulcrologic.fulcro-i18n.i18n :refer [tr]]]
        :clj [[com.fulcrologic.fulcro-i18n.i18n :refer [tr]]])
    [com.fulcrologic.rad.rendering.gluestack-ui.utils :refer [transit->str str->transit]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.gluestack-ui.lucide-icons :refer [chevron-down-icon]]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control :refer [ui-form-control
                                                                     ui-form-control-label
                                                                     ui-form-control-label-text
                                                                     ui-form-control-error
                                                                     ui-form-control-error-text]]
    [com.fulcrologic.gluestack-ui.components.ui.select :refer [ui-select
                                                               ui-select-trigger
                                                               ui-select-input
                                                               ui-select-icon
                                                               ui-select-portal
                                                               ui-select-backdrop
                                                               ui-select-content
                                                               ui-select-scroll-view
                                                               ui-select-item
                                                               ui-select-drag-indicator-wrapper
                                                               ui-select-drag-indicator]]
    [com.fulcrologic.gluestack-ui.components.ui.safe-area-view :refer [ui-safe-area-view]]
    [com.fulcrologic.gluestack-ui.components.ui.checkbox :refer [ui-checkbox
                                                                 ui-checkbox-indicator
                                                                 ui-checkbox-icon
                                                                 ui-checkbox-label]]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.rad.rendering.gluestack-ui.form-options :as gufo]
    [taoensso.timbre :as log]))

(defn enumerated-options
  "Converts attribute enumerated-values into label/value option pairs for rendering."
  [{::form/keys [form-instance] :as env} {::attr/keys [qualified-key] :as attribute}]
  (let [{::attr/keys [enumerated-values]} attribute
        enumeration-labels (merge
                             (::attr/enumerated-labels attribute)
                             (comp/component-options form-instance ::form/enumerated-labels qualified-key))]
    ;; Sort by label text
    (sort-by :label
      (mapv (fn [k]
              {:label (?! (get enumeration-labels k (name k)))
               :value k}) enumerated-values))))

(defsc EnumeratedSelectField [this {:keys [env attribute]}]
  {:shouldComponentUpdate (fn [_ _ _] true)}
  (let [{::form/keys [master-form form-instance]} env
        {::attr/keys [qualified-key computed-options required?]} attribute
        visible? (form/field-visible? form-instance attribute)]

    (when visible?
      (let [props              (comp/props form-instance)
            read-only?         (form/read-only? form-instance attribute)
            invalid?           (form/invalid-attribute-value? env attribute)
            omit-label?        (form/omit-label? form-instance attribute)
            user-props         (?! (form/field-style-config env attribute :input/props) env)
            field-label        (form/field-label env attribute)
            validation-message (when invalid? (form/validation-error-message env attribute))
            options            (or (?! computed-options env) (enumerated-options env attribute))
            top-class          (gufo/top-class form-instance attribute)
            value              (get props qualified-key)
            placeholder        (tr "Select an option")
            selected-option    (first (filter #(= value (:value %)) options))
            selected-label     (when selected-option (:label selected-option))]

        (ui-box {:key (str qualified-key)}
          (ui-form-control
            {:size       "md"
             :isReadOnly read-only?
             :isInvalid  invalid?
             :isRequired required?
             :className  (or top-class "")}

            (when-not omit-label?
              (ui-form-control-label {}
                (ui-form-control-label-text {} field-label)))

            (ui-select
              {:selectedValue (transit->str value)
               :onValueChange (fn [encoded-v]
                                (form/input-changed! env qualified-key (str->transit encoded-v)))
               :isDisabled    read-only?}

              ;; The select trigger/button
              (ui-select-trigger
                {:size "md" :className "bg-background-0"}
                (ui-select-input {:className   "flex-1"
                                  :placeholder placeholder
                                  :value       selected-label})
                (ui-select-icon {:className "mr-3" :as chevron-down-icon}))

              ;; The dropdown content
              (when (not read-only?)
                (ui-select-portal {}
                  (ui-select-backdrop {})
                  (ui-select-content {:style {:maxHeight "60%"}}
                    (ui-select-drag-indicator-wrapper {}
                      (ui-select-drag-indicator {}))
                    (ui-select-scroll-view {}
                      (for [{:keys [label value]} options]
                        (ui-select-item {:key   (transit->str value)
                                         :value (transit->str value)
                                         :label (ui-box {} label)})))
                    (ui-safe-area-view {})))))
            (ui-form-control-error {}
              (ui-form-control-error-text {} validation-message))))))))

(def ui-enumerated-select-field (comp/factory EnumeratedSelectField {:keyfn (fn [{:keys [attribute]}] (::attr/qualified-key attribute))}))

(defn render-field [env attribute]
  (ui-enumerated-select-field {:env env :attribute attribute}))
