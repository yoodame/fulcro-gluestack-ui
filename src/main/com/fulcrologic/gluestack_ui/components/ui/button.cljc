(ns com.fulcrologic.gluestack-ui.components.ui.button
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory ]]
    #?(:cljs ["@/components/ui/button/index.js" :refer [Button ButtonText ButtonSpinner ButtonIcon ButtonGroup]])))

(def ui-button
  "ButtonFactory"
  #?(:cljs (react-factory Button)))

(def ui-button-text
  "ButtonTextFactory"
  #?(:cljs (react-factory ButtonText)))

(def ui-button-spinner
  "ButtonSpinnerFactory"
  #?(:cljs (react-factory ButtonSpinner)))

(def ui-button-icon
  "ButtonIconFactory"
  #?(:cljs ButtonIcon))

(def ui-button-group
  "ButtonGroupFactory"
  #?(:cljs (react-factory ButtonGroup)))
