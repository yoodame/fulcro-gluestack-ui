(ns com.fulcrologic.gluestack-ui.components.ui.fab
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/fab/index.js" :refer [Fab FabLabel FabIcon]])))

(def ui-fab
  "Fab Factory"
  #?(:cljs (react-factory Fab)))

(def ui-fab-label
  "FabLabel Factory"
  #?(:cljs (react-factory FabLabel)))

(def ui-fab-icon
  "FabIcon Factory"
  #?(:cljs (react-factory FabIcon)))
