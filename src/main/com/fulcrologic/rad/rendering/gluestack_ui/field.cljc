(ns com.fulcrologic.rad.rendering.gluestack-ui.field
  (:require
    [clojure.string :as str]
    [com.fulcrologic.gluestack-ui.components.ui.form-control
     :refer [ui-form-control
             ui-form-control-label ui-form-control-label-text ui-form-control-label-astrick
             ui-form-control-error ui-form-control-error-text ui-form-control-error-icon
             ui-form-control-helper ui-form-control-helper-text]]
    [com.fulcrologic.rad.attributes :as attr]
    ;[com.fulcrologic.fulcro.dom.html-entities :as ent]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.rendering.gluestack-ui.form-options :as gufo]
    [taoensso.timbre :as log]))

(defn render-field-factory
  "Create a general field factory using the given input factory as the function to call to draw an input."
  ([input-factory]
   (render-field-factory {} input-factory))
  ([addl-props input-factory]
   (fn [{::form/keys [form-instance] :as env} {::attr/keys [qualified-key required?] :as attribute}]
     (form/with-field-context [{:keys [value field-style-config
                                       visible? read-only?
                                       validation-message
                                       omit-label?
                                       field-label invalid?]} (form/field-context env attribute)
                               addl-props (-> field-style-config
                                            (merge addl-props)
                                            (cond->
                                              invalid? (assoc :isInvalid true)
                                              read-only? (assoc :isReadOnly true)))]
       (let [top-class (gufo/top-class form-instance attribute)]
         (when visible?
           (ui-form-control {:key     (str qualified-key)
                             :size    (or (:size addl-props) "md")
                             :isReadOnly read-only?
                             :isInvalid invalid?
                             :isRequired required?
                             :className (or top-class "")}
             (when-not omit-label?
               (ui-form-control-label {}
                 (ui-form-control-label-text {} (or field-label (some-> qualified-key name str/capitalize)))))
             (input-factory (merge addl-props
                              {:value    value
                               :onBlur   (fn [v] (form/input-blur! env qualified-key v))
                               :onChangeText (fn [v] (form/input-changed! env qualified-key v))}))
             (when (and invalid? omit-label?)
               (ui-form-control-error {}
                 (ui-form-control-error-text {} validation-message))))))))))
