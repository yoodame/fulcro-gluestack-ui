(ns com.fulcrologic.gluestack-ui.components.ui.scroll-view
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/scroll-view/index.js" :refer [ScrollView]])))

(def ui-scroll-view
  "ScrollView Factory"
  #?(:cljs (react-factory ScrollView)))
