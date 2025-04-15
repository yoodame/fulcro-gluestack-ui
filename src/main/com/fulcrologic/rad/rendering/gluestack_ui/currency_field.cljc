(ns com.fulcrologic.rad.rendering.gluestack-ui.currency-field
  "GlueStack UI renderer for currency fields in Fulcro RAD forms.
   Renders decimal values with USD currency formatting."
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.gluestack-ui.components.ui.input :refer [ui-input ui-input-field]]
    [com.fulcrologic.rad.rendering.gluestack-ui.field :refer [render-field-factory]]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.type-support.decimal :as math]
    [clojure.string :as str]))

(defsc CurrencyInput [this {:keys [env attribute field-context]}]
  {:initLocalState        (fn [_] {:editing? false})
   :shouldComponentUpdate (fn [_ _ _] true)}
  (let [{:keys [value onChangeText onBlur onFocus placeholder :as field-style-config]} (:field-style-config field-context)
        editing?     (comp/get-state this :editing?)
        input-props  (-> field-style-config
                       (dissoc :value :onChangeText :onBlur :onFocus)
                       (assoc :keyboardType "numeric"))

        ;; Format the value as a currency string for display
        string-value (if (and value (not editing?))
                       (math/numeric->currency-str value)
                       (if value
                         (str/replace (math/numeric->str value) #"[$,]" "")
                         ""))

        ;; Handle numeric conversions
        on-change-fn (fn [text]
                       ;; Filter to allow only valid decimal input
                       (let [filtered-text (if (empty? text)
                                             ""
                                             (let [decimal-regex #"^-?\d*\.?\d*$"]
                                               (if (re-matches decimal-regex text)
                                                 text
                                                 (str/replace string-value #"[$,]" ""))))
                             numeric-value (when (seq filtered-text)
                                             (math/numeric filtered-text))]
                         (when onChangeText
                           (onChangeText numeric-value))))

        ;; On focus, switch to editing mode (plain number)
        on-focus-fn  (fn [e]
                       (comp/set-state! this {:editing? true})
                       (when onFocus
                         (onFocus e)))

        ;; On blur, format as currency and leave editing mode
        on-blur-fn   (fn [e]
                       (comp/set-state! this {:editing? false})
                       (when onBlur
                         (onBlur e)))]

    (ui-input input-props
      (ui-input-field {:value        string-value
                       :placeholder  (or placeholder "Enter amount...")
                       :onChangeText on-change-fn
                       :onFocus      on-focus-fn
                       :onBlur       on-blur-fn}))))

(def ui-currency-input (comp/factory CurrencyInput {:keyfn (fn [{:keys [attribute]}]
                                                             (::attr/qualified-key attribute))}))

(def render-field (render-field-factory ui-currency-input))
