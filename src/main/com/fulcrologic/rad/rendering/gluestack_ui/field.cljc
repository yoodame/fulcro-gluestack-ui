(ns com.fulcrologic.rad.rendering.gluestack-ui.field
  (:require
    [clojure.string :as str]
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.gluestack-ui.components.ui.form-control
     :refer [ui-form-control
             ui-form-control-label ui-form-control-label-text ui-form-control-label-astrick
             ui-form-control-error ui-form-control-error-text ui-form-control-error-icon
             ui-form-control-helper ui-form-control-helper-text]]
    [com.fulcrologic.rad.attributes :as attr]
    ;[com.fulcrologic.fulcro.dom.html-entities :as ent]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.rendering.gluestack-ui.form-options :as gufo]
    [taoensso.timbre :as log]))

(defn render-field-with-context
  [{::form/keys [form-instance] :as env} {::attr/keys [qualified-key required?] :as attribute} addl-props field-factory]
  (form/with-field-context [{:keys [value field-style-config
                                    visible? read-only?
                                    validation-message
                                    omit-label?
                                    field-label invalid?]} (form/field-context env attribute)
                            disabled?          (get field-style-config :isDisabled read-only?)
                            field-style-config (-> field-style-config
                                                 (merge {:value        value
                                                         :onBlur       (fn [v] (form/input-blur! env qualified-key v))
                                                         :onChangeText (fn [v] (form/input-changed! env qualified-key v))})
                                                 (merge addl-props))
                            field-context      {:value              value
                                                :field-style-config field-style-config
                                                :visible?           visible?
                                                :read-only?         read-only?
                                                :validation-message validation-message
                                                :omit-label?        omit-label?
                                                :field-label        field-label
                                                :invalid?           invalid?}]
    (let [options    (comp/component-options form-instance)
          ref-field? (fo/subform-options options qualified-key)
          top-class  (gufo/top-class form-instance attribute)]
      (when visible?
        (ui-form-control {:key        (str qualified-key)
                          :size       (or (:size field-style-config) "md")
                          :isReadOnly read-only?
                          :isInvalid  invalid?
                          :isRequired required?
                          :isDisabled disabled?
                          :className  (or top-class "")}

          (when-not omit-label?
            (ui-form-control-label {}
              (ui-form-control-label-text {}
                (or field-label (some-> qualified-key name str/capitalize)))))

          (field-factory {:env           env
                          :attribute     attribute
                          :options       options
                          :field-context field-context})

          (when-not ref-field? (ui-form-control-error {}
                                 (ui-form-control-error-text {} validation-message))))))))


(defn render-field-factory
  "Create a general field factory using the given input factory as the function to call to draw an input."
  ([input-factory]
   (render-field-factory {} input-factory))
  ([addl-props input-factory]
   (fn [{::form/keys [form-instance] :as env} {::attr/keys [qualified-key required?] :as attribute}]
     (render-field-with-context env attribute addl-props input-factory))))
