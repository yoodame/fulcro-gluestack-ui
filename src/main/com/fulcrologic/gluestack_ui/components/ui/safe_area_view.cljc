(ns com.fulcrologic.gluestack-ui.components.ui.safe-area-view
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer []]
    #?(:cljs ["@/components/ui/safe-area-view/index.js" :refer [SafeAreaView]])))

(def ui-safe-area-view
  "React native factory for SafeAreaView"
  #?(:cljs SafeAreaView))
