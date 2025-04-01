(ns com.fulcrologic.rad.rendering.gluestack-ui.enumerated-field
  "GlueStack UI renderer for enumerated fields in Fulcro RAD forms.
   Renders enum and keyword values as select dropdowns and checkbox groups."
  (:require
   [clojure.string :as str]
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   #?(:cljs [com.fulcrologic.fulcro-i18n.i18n :refer [tr]])
   #?(:clj [com.fulcrologic.fulcro-i18n.i18n :refer [tr]])
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
                                                             ui-select-item]]
   [com.fulcrologic.gluestack-ui.components.ui.checkbox :refer [ui-checkbox
                                                               ui-checkbox-indicator
                                                               ui-checkbox-icon
                                                               ui-checkbox-label]]
   [com.fulcrologic.rad.attributes :as attr]
   [com.fulcrologic.rad.form :as form]
   [com.fulcrologic.rad.options-util :refer [?!]]
   [com.fulcrologic.rad.rendering.gluestack-ui.form-options :as gufo]))

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

(defn- render-to-many [{::form/keys [form-instance] :as env} {::attr/keys [qualified-key computed-options] :as attribute}]
  (when (form/field-visible? form-instance attribute)
    (let [props        (comp/props form-instance)
          read-only?   (form/read-only? form-instance attribute)
          invalid?     (form/invalid-attribute-value? env attribute)
          required?    (::attr/required? attribute)
          omit-label?  (form/omit-label? form-instance attribute)
          field-label  (form/field-label env attribute)
          validation-message (when invalid? (form/validation-error-message env attribute))
          options      (or (?! computed-options env) (enumerated-options env attribute))
          top-class    (gufo/top-class form-instance attribute)
          selected-ids (set (get props qualified-key))]

      (ui-box {:key (str qualified-key)}
        (ui-form-control
         {:size      "md"
          :isReadOnly read-only?
          :isInvalid  invalid?
          :isRequired required?
          :className  (or top-class "")}

         (when-not omit-label?
           (ui-form-control-label {}
             (ui-form-control-label-text {} field-label)))

         ;; Render each option as a checkbox for multi-select
         (ui-box {:mt "2"}
           (for [{:keys [label value]} options]
             (let [checked? (contains? selected-ids value)]
               (ui-box {:key value :mb "2"}
                 (ui-checkbox
                  {:value      (str value)
                   :isDisabled read-only?
                   :isChecked  checked?
                   :onChange   (fn [_]
                                 (let [selection (if-not checked?
                                                  (conj (set (or selected-ids #{})) value)
                                                  (disj selected-ids value))]
                                   (form/input-changed! env qualified-key selection)))}
                  (ui-checkbox-indicator {}
                    (ui-checkbox-icon {:size "md"}))
                  (ui-checkbox-label {:_text {:fontSize "sm"}} label)))))))

        (when (and invalid? validation-message)
          (ui-form-control-error {}
            (ui-form-control-error-text {} validation-message)))))))

(defn- render-to-one [{::form/keys [form-instance] :as env} {::attr/keys [qualified-key computed-options required?] :as attribute}]
  (when (form/field-visible? form-instance attribute)
    (let [props       (comp/props form-instance)
          read-only?  (form/read-only? form-instance attribute)
          invalid?    (form/invalid-attribute-value? env attribute)
          omit-label? (form/omit-label? form-instance attribute)
          user-props  (?! (form/field-style-config env attribute :input/props) env)
          field-label (form/field-label env attribute)
          validation-message (when invalid? (form/validation-error-message env attribute))
          options     (or (?! computed-options env) (enumerated-options env attribute))
          top-class   (gufo/top-class form-instance attribute)
          value       (get props qualified-key)
          placeholder (tr "Select an option")]

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

         ;; If read-only, just display the selected value text
         (if read-only?
           (let [selected-option (first (filter #(= value (:value %)) options))]
             (ui-box {:p "2" :borderWidth "1" :borderRadius "md" :borderColor "gray.300"}
               (ui-box {} (:label selected-option))))

           ;; Otherwise render the select dropdown
           (ui-select
            {:selectedValue (str value)
             :onValueChange (fn [v]
                              ;; Convert string value back to appropriate type
                              (let [parsed-value (cond
                                                   ;; Handle keywords
                                                   (keyword? value) (keyword v)

                                                   ;; Handle integers and longs
                                                   #?(:cljs
                                                      (number? value)
                                                      :clj
                                                      (or (instance? Integer value)
                                                          (instance? Long value)))
                                                   #?(:cljs
                                                      (js/parseInt v 10)
                                                      :clj
                                                      (Long/parseLong v))

                                                   ;; Default - keep as string
                                                   :else v)]
                                (form/input-changed! env qualified-key parsed-value)))
             :isDisabled    read-only?}

            ;; The select trigger/button
            (ui-select-trigger
             {:className (str "border border-gray-300 rounded-md px-3 py-2 "
                             (when invalid? "border-red-500"))
              :style {:width "100%"}
              :borderWidth "1"
              :borderRadius "md"}
             (ui-select-input
              {:placeholder placeholder})
             (ui-select-icon {}))

            ;; The dropdown content
            (ui-select-portal {}
              (ui-select-backdrop {})
              (ui-select-content
               {:style {:width "90%", :maxHeight "40%"}}
               (ui-box {:py "2"}
                 (for [{:keys [label value]} options]
                   (ui-select-item
                    {:key   (str value)
                     :label label
                     :value (str value)}))))))))

        (when (and invalid? validation-message)
          (ui-form-control-error {}
            (ui-form-control-error-text {} validation-message)))))))

(defn render-field [env {::attr/keys [cardinality] :or {cardinality :one} :as attribute}]
  (if (= :many cardinality)
    (render-to-many env attribute)
    (render-to-one env attribute)))
