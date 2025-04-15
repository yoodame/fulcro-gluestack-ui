(ns com.fulcrologic.rad.rendering.gluestack-ui.int-field
  "GlueStack UI renderer for integer fields in Fulcro RAD forms.
   Renders integer and long values as numeric input fields."
  (:require
    [com.fulcrologic.gluestack-ui.components.ui.input :refer [ui-input ui-input-field]]
    [com.fulcrologic.rad.rendering.gluestack-ui.field :refer [render-field-factory]]
    [com.fulcrologic.fulcro.dom.events :as evt]))

(defn- int-input
  "Renders a numeric input field that converts string values to integers.
   Used internally by the render-field function."
  [{:keys [env attribute field-context]}]
  (let [{:keys [onChangeText :as field-style-config]} (:field-style-config field-context)
        input-props       (cond-> field-style-config
                            true (dissoc :placeholder :onBlur :onChangeText :value)
                            (not (:className field-style-config)) (assoc :className "my-1 bg-background-0"))

        ;; Convert string inputs to integers (or nil if invalid)
        parse-int-fn      (fn [s]
                            (let [v (js/parseInt s 10)]
                              (when-not (js/isNaN v) v)))

        ;; Handle integer-specific input value changes
        on-change-fn      (fn [v]
                            (when onChangeText
                              (onChangeText (parse-int-fn v))))

        input-field-props (-> (select-keys field-style-config [:placeholder :onBlur :value])
                            (assoc :type "number"
                                   :keyboardType "numeric"
                                   :onChangeText on-change-fn))]

    (ui-input input-props
      (ui-input-field input-field-props))))

(def render-field (render-field-factory int-input))
