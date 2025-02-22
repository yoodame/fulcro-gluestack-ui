(ns com.fulcrologic.gluestack-ui.components.ui.button
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/button/index.js" :refer [Button ButtonText ButtonSpinner ButtonIcon ButtonGroup]])))

(def ui-button
  "Button Factory"
  #?(:cljs (react-factory Button)))

(def ui-button-text
  "ButtonText Factory"
  #?(:cljs (react-factory ButtonText)))

(def ui-button-spinner
  "ButtonSpinner Factory"
  #?(:cljs (react-factory ButtonSpinner)))

(def ui-button-icon
  "ButtonIcon Factory"
  #?(:cljs (react-factory ButtonIcon)))

(def ui-button-group
  "ButtonGroup Factory"
  #?(:cljs (react-factory ButtonGroup)))
