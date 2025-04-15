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
    [com.fulcrologic.rad.rendering.gluestack-ui.field :refer [render-field-factory]]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
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
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.options-util :refer [?!]]
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

(defsc EnumeratedSelectField [this {:keys [env attribute field-context]}]
  {:shouldComponentUpdate (fn [_ _ _] true)}
  (let [{::form/keys [master-form form-instance]} env
        {::attr/keys [qualified-key computed-options required?]} attribute]

    (let [value           (:value field-context)
          placeholder     (get-in field-context [:field-style-config :placeholder] (tr "Select an option"))
          options         (or (?! computed-options env) (enumerated-options env attribute))
          selected-option (first (filter #(= value (:value %)) options))
          selected-label  (when selected-option (:label selected-option))]

      (ui-select
        {:selectedValue (transit->str value)
         :onValueChange (fn [encoded-v]
                          (form/input-changed! env qualified-key (str->transit encoded-v)))}

        ;; The select trigger/button
        (ui-select-trigger
          {:size "md" :className "bg-background-0"}
          (ui-select-input {:className   "flex-1"
                            :placeholder placeholder
                            :value       selected-label})
          (ui-select-icon {:className "mr-3" :as chevron-down-icon}))

        ;; The dropdown content
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
            (ui-safe-area-view {})))))))

(def ui-enumerated-select-field (comp/factory EnumeratedSelectField {:keyfn (fn [{:keys [attribute]}]
                                                                              (::attr/qualified-key attribute))}))

(def render-field (render-field-factory ui-enumerated-select-field))
