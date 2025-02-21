(ns com.fulcrologic.gluestack-ui.components.ui.hstack
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/hstack/index.js" :refer [HStack]])))

(def ui-h-stack
  "React native factory for HStack"
  #?(:cljs (react-factory HStack)))
