(ns com.fulcrologic.gluestack-ui.components.ui.pressable
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/pressable/index.js" :refer [Pressable]])))

(def ui-pressable
  "Pressable Factory"
  #?(:cljs (react-factory Pressable)))
