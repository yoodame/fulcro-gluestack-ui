(ns com.fulcrologic.gluestack-ui.components.ui.vstack
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/vstack/index.js" :refer [VStack]])))

(def ui-v-stack
  "VStackFactory"
  #?(:cljs (react-factory VStack)))
