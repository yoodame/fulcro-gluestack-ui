(ns com.fulcrologic.rad.rendering.gluestack-ui.decimal-field
  "GlueStack UI renderer for decimal fields in Fulcro RAD forms.
   Renders decimal values with proper formatting and validation."
  (:require
   [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
   [com.fulcrologic.fulcro.dom.events :as evt]
   [com.fulcrologic.gluestack-ui.components.ui.input :refer [ui-input ui-input-field]]
   [com.fulcrologic.rad.rendering.gluestack-ui.field :refer [render-field-factory]]
   [com.fulcrologic.rad.type-support.decimal :as math]
   [clojure.string :as str]))

(defsc DecimalInput [this {:keys [value onChangeText onBlur] :as props}]
  {:shouldComponentUpdate (fn [_ _ _] true)}
  (let [input-props (-> props
                       (dissoc :value :onChangeText :onBlur)
                       (assoc :keyboardType "numeric"))

        ;; Format the value as a string for display
        string-value (if value
                       (math/numeric->str value)
                       "")

        ;; Handle numeric conversions
        on-change-fn (fn [text]
                       ;; Filter to allow only valid decimal input
                       (let [filtered-text (if (empty? text)
                                             ""
                                             (let [decimal-regex #"^-?\d*\.?\d*$"]
                                               (if (re-matches decimal-regex text)
                                                 text
                                                 string-value)))
                             numeric-value (when (seq filtered-text)
                                             (math/numeric filtered-text))]
                         (when onChangeText
                           (onChangeText numeric-value))))

        ;; On blur, format the number properly
        on-blur-fn (fn [e]
                     (when onBlur
                       (onBlur e)))]

    (ui-input input-props
      (ui-input-field {:value string-value
                      :placeholder (or (:placeholder props) "Enter a number...")
                      :onChangeText on-change-fn
                      :onBlur on-blur-fn}))))

(def ui-decimal-input (comp/factory DecimalInput))

(def render-field
  "Renders a decimal field that properly handles numeric input and formatting."
  (render-field-factory ui-decimal-input))
