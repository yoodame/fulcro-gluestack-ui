(ns com.fulcrologic.gluestack-ui.components.ui.grid
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/grid/index.js" :refer [Grid GridItem]])))

(def ui-grid
  "Grid Factory"
  #?(:cljs (react-factory Grid)))

(def ui-grid-item
  "GridItem Factory"
  #?(:cljs (react-factory GridItem)))
