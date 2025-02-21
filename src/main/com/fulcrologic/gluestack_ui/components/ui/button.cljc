(ns com.fulcrologic.gluestack-ui.components.ui.button
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory ]]
    #?(:cljs ["@/components/ui/button/index.js" :refer [Button ButtonText ButtonSpinner ButtonIcon ButtonGroup]])))

(def ui-button
  "React native factory for Button"
  #?(:cljs (react-factory Button)))

(def ui-button-text
  "React native factory for ButtonText"
  #?(:cljs (react-factory ButtonText)))

(def ui-button-spinner
  "React native factory for ButtonSpinner"
  #?(:cljs (react-factory ButtonSpinner)))

(def ui-button-icon
  "React native factory for ButtonIcon"
  #?(:cljs ButtonIcon))

(def ui-button-group
  "React native factory for ButtonGroup"
  #?(:cljs (react-factory ButtonGroup)))
