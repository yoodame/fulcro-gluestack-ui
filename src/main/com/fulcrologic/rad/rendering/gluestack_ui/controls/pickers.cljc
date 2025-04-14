(ns com.fulcrologic.rad.rendering.gluestack-ui.controls.pickers
  (:require
    #?@(:cljs [[cognitect.transit :as transit]
               [com.fulcrologic.fulcro.dom :as dom]])
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.raw.components :as rc]
    [com.fulcrologic.rad.picker-options :as po]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.rad.rendering.gluestack-ui.utils :refer [transit->str str->transit]]
    [com.fulcrologic.gluestack-ui.lucide-icons :refer [chevron-down-icon]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control :refer [ui-form-control
                                                                     ui-form-control-helper-text]]
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
    [com.fulcrologic.gluestack-ui.components.ui.safe-area-view :refer [ui-safe-area-view]]))

(defsc SimplePicker [_ {:keys [instance control-key]}]
  {:shouldComponentUpdate (fn [_ _ _] true)
   :componentDidMount     (fn [this]
                            (let [{:keys [instance control-key] :as props} (comp/props this)
                                  controls (control/component-controls instance)
                                  {::po/keys [query-key] :as picker-options} (get controls control-key)]
                              (when query-key
                                (po/load-picker-options! instance (comp/react-type instance) props picker-options))))}
  (let [controls (control/component-controls instance)
        {:keys [label onChange disabled? visible? action placeholder options user-props helper-text] :as control} (get controls control-key)
        options  (or (?! options instance)
                   (po/current-picker-options instance control))]
    (when control
      (let [label           (or (?! label instance))
            disabled?       (?! disabled? instance)
            placeholder     (?! placeholder instance)
            visible?        (or (nil? visible?) (?! visible? instance))
            value           (control/current-value instance control-key)
            selected-option (first (filter #(= value (:value %)) options))
            selected-label  (when selected-option (:label selected-option))]
        (when visible?
          (ui-form-control {:key       (str control-key)
                            :className "flex"
                            :size      "sm"}
            (ui-select
              (merge
                {:isDisabled    disabled?
                 :selectedValue (transit->str value)
                 :onValueChange (fn [encoded-v]
                                  (let [v             (str->transit encoded-v)
                                        current-value (control/current-value instance control-key)
                                        new-value     (if (= v current-value) nil v)]
                                    (control/set-parameter! instance control-key new-value)
                                    (binding [rc/*after-render* true]
                                      (when onChange
                                        (onChange instance new-value))
                                      (when action
                                        (action instance)))))}
                user-props)
              (ui-select-trigger
                {:variant "rounded" :size "sm" :className "bg-background-0"}
                (ui-select-input {:placeholder placeholder
                                  :value       selected-label})
                (ui-select-icon {:className "mr-2" :as chevron-down-icon}))

              (ui-select-portal {}
                (ui-select-backdrop {})
                (ui-select-content {:style {:maxHeight "60%"}}
                  (ui-select-drag-indicator-wrapper {}
                    (ui-select-drag-indicator {}))
                  (ui-select-scroll-view {}
                    (for [{:keys [label value]} options]
                      (ui-select-item {:key   (transit->str value)
                                       :value (transit->str value)
                                       :label label})))
                  (ui-safe-area-view {}))))
            (when helper-text
              (ui-form-control-helper-text {} helper-text))))))))

(def render-control (comp/factory SimplePicker {:keyfn :control-key}))
