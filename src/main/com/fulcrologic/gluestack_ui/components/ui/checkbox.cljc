(ns com.fulcrologic.gluestack-ui.components.ui.checkbox
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/checkbox/index.js" :refer [Checkbox CheckboxIndicator CheckboxLabel CheckboxIcon CheckboxGroup]])))

(def ui-checkbox
  "Checkbox Factory"
  #?(:cljs (react-factory Checkbox)))

(def ui-checkbox-indicator
  "CheckboxIndicator Factory"
  #?(:cljs (react-factory CheckboxIndicator)))

(def ui-checkbox-label
  "CheckboxLabel Factory"
  #?(:cljs (react-factory CheckboxLabel)))

(def ui-checkbox-icon
  "CheckboxIcon Factory"
  #?(:cljs (react-factory CheckboxIcon)))

(def ui-checkbox-group
  "CheckboxGroup Factory"
  #?(:cljs (react-factory CheckboxGroup)))
