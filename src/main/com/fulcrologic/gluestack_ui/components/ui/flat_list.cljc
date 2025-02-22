(ns com.fulcrologic.gluestack-ui.components.ui.flat-list
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/flat-list/index.js" :refer [FlatList]])))

(def ui-flat-list
  "FlatList Factory"
  #?(:cljs (react-factory FlatList)))
