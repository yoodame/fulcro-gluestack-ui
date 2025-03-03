(ns com.fulcrologic.gluestack-ui.components.ui.alert
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/alert/index.js" :refer [Alert AlertText AlertIcon UIAlert]])))

(def ui-alert
  "Alert Factory"
  #?(:cljs (react-factory Alert)))

(def ui-alert-text
  "AlertText Factory"
  #?(:cljs (react-factory AlertText)))

(def ui-alert-icon
  "AlertIcon Factory"
  #?(:cljs (react-factory AlertIcon)))

(def ui-u-i-alert
  "UIAlert Factory"
  #?(:cljs (react-factory UIAlert)))
