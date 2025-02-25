(ns com.fulcrologic.rad.rendering.gluestack-ui.controls.action-button
  (:require
    [taoensso.timbre :as log]
    [com.fulcrologic.rad.report :as report]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.rad.gluestack-ui-options :as guo]
    [com.fulcrologic.fulcro.data-fetch :as df]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.gluestack-ui.components.ui.button :refer [ui-button ui-button-text ui-button-icon]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]))

(defsc ActionButton [_ {:keys [instance control-key]}]
  {:shouldComponentUpdate (fn [_ _ _] true)}
  (let [controls (control/component-controls instance)
        render   (guo/get-rendering-options instance guo/action-button-render)
        props    (comp/props instance)
        {:keys [label icon class action disabled? visible? size variant variant-action]} control]
    (when control
      (let [label     (?! label instance)
            class     (?! class instance)
            loading?  (df/loading? (get-in props [df/marker-table (comp/get-ident instance)]))
            disabled? (or loading? (?! disabled? instance))
            visible?  (or (nil? visible?) (?! visible? instance))
            onPress   (fn [] (when action (action instance control-key)))]
        (when visible?
          (or
            (?! render instance (merge control
                                  {:key       control-key
                                   :label     label
                                   :class     class
                                   :onPress   onPress
                                   :disabled? disabled?
                                   :loading?  loading?}))
            (ui-button
              (cond-> {:key       (str control-key)
                       :className class
                       :size "sm"
                       :disabled  (boolean disabled?)
                       :onPress   onPress}
                size (assoc :size size)
                variant (assoc :variant variant)
                variant-action (assoc :action variant-action))
              (when icon (ui-button-icon {:as icon :className ""}))
              (when label (ui-button-text {} label)))))))))

(def render-control (comp/factory ActionButton {:keyfn :control-key}))
