(ns com.fulcrologic.rad.rendering.gluestack-ui.controls.control
  "Generic control framework for GlueStack UI controls.
   Used by specialized control types like date pickers, action buttons, etc."
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.text :refer [ui-text]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control :refer [ui-form-control
                                                                     ui-form-control-label
                                                                     ui-form-control-label-text]]
    [taoensso.timbre :as log]))

(defsc Control [_ {:keys [instance control control-key input-factory] :as render-env}]
  {:shouldComponentUpdate (fn [_ _ _] true)}
  (let [controls (control/component-controls instance)
        {:keys [label onChange action disabled? visible? user-props] :as control} (get controls control-key control)]
    (if (and input-factory control)
      (let [label     (or (?! label instance))
            disabled? (?! disabled? instance)
            visible?  (or (nil? visible?) (?! visible? instance))
            value     (control/current-value instance control-key)
            onChange  (fn [new-value]
                        (control/set-parameter! instance control-key new-value)
                        (when onChange
                          (onChange instance new-value))
                        (when action
                          (action instance)))]
        (when visible?
          (ui-form-control {:key (str control-key) :size "md"}
            (when label
              (ui-form-control-label {}
                (ui-form-control-label-text {} label)))
            (input-factory render-env (merge user-props
                                         {:disabled? disabled?
                                          :isDisabled disabled?
                                          :value     value
                                          :onChange  onChange})))))
      (log/error "Cannot render control. Missing input factory or control definition."))))

(def ui-control (comp/factory Control {:keyfn :control-key}))
