(ns com.fulcrologic.gluestack-ui.components.ui.radio
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/radio/index.js" :refer [Radio RadioGroup RadioIndicator RadioLabel RadioIcon]])))

(def ui-radio
  "Radio Factory"
  #?(:cljs (react-factory Radio)))

(def ui-radio-group
  "RadioGroup Factory"
  #?(:cljs (react-factory RadioGroup)))

(def ui-radio-indicator
  "RadioIndicator Factory"
  #?(:cljs (react-factory RadioIndicator)))

(def ui-radio-label
  "RadioLabel Factory"
  #?(:cljs (react-factory RadioLabel)))

(def ui-radio-icon
  "RadioIcon Factory"
  #?(:cljs (react-factory RadioIcon)))
