(ns com.fulcrologic.gluestack-ui.components.ui.vstack
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/vstack/index.js" :refer [VStack]])))

(def ui-v-stack
  "React native factory for VStack"
  #?(:cljs (react-factory VStack)))
