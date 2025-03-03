(ns com.fulcrologic.gluestack-ui.components.ui.virtualized-list
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/virtualized-list/index.js" :refer [VirtualizedList]])))

(def ui-virtualized-list
  "VirtualizedList Factory"
  #?(:cljs (react-factory VirtualizedList)))
