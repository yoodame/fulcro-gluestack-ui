(ns com.fulcrologic.gluestack-ui.components.ui.status-bar
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/status-bar/index.js" :refer [StatusBar]])))

(def ui-status-bar
  "StatusBar Factory"
  #?(:cljs (react-factory StatusBar)))
