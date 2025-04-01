(ns com.fulcrologic.rad.rendering.gluestack-ui.boolean-field
  "GlueStack UI renderer for boolean fields in Fulcro RAD forms.
   Renders boolean values as checkboxes with labels."
  (:require
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.gluestack-ui.components.ui.checkbox :refer [ui-checkbox ui-checkbox-indicator
                                                                ui-checkbox-icon ui-checkbox-label]]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control :refer [ui-form-control
                                                                    ui-form-control-label
                                                                    ui-form-control-label-text
                                                                    ui-form-control-error
                                                                    ui-form-control-error-text]]
    [com.fulcrologic.rad.rendering.gluestack-ui.form-options :as gufo]
    [clojure.string :as str]))

(defn render-field
  "Render a boolean field as a GlueStack UI checkbox.
   Supports standard RAD field options."
  [{::form/keys [form-instance] :as env} attribute]
  (let [k                 (::attr/qualified-key attribute)
        props             (comp/props form-instance)
        user-props        (?! (form/field-style-config env attribute :input/props) env)
        field-label       (form/field-label env attribute)
        visible?          (form/field-visible? form-instance attribute)
        read-only?        (form/read-only? form-instance attribute)
        invalid?          (form/invalid-attribute-value? env attribute)
        required?         (::attr/required? attribute)
        omit-label?       (form/omit-label? form-instance attribute)
        validation-message (when invalid? (form/validation-error-message env attribute))
        top-class         (gufo/top-class form-instance attribute)
        value             (get props k false)

        ;; Component prop maps
        form-control-props {:size       "md"
                            :isReadOnly  read-only?
                            :isInvalid   invalid?
                            :isRequired  required?
                            :className   (or top-class "")}

        checkbox-props    (merge
                            {:value      value
                             :isDisabled read-only?
                             :isChecked  value
                             :onChange   (fn [evt]
                                           (let [v (not value)]
                                             (form/input-blur! env k v)
                                             (form/input-changed! env k v)))}
                            user-props)

        checkbox-icon-props {:size "md"}

        checkbox-label-props {:_text {:fontSize "sm"}}]

    (when visible?
      (ui-box {:key (str k)}
        (ui-form-control form-control-props
          (when-not omit-label?
            (ui-form-control-label {}
              (ui-form-control-label-text {} (or field-label (some-> k name str/capitalize)))))

          (ui-checkbox checkbox-props
            (ui-checkbox-indicator {}
              (ui-checkbox-icon checkbox-icon-props))
            (when field-label
              (ui-checkbox-label checkbox-label-props (or field-label (some-> k name str/capitalize))))))

        (when (and invalid? validation-message)
          (ui-form-control-error {}
            (ui-form-control-error-text {} validation-message)))))))
