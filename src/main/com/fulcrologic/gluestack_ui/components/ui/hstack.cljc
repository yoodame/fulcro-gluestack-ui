(ns com.fulcrologic.gluestack-ui.components.ui.hstack
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/hstack/index.js" :refer [HStack]])))

(def ui-h-stack
  "HStackFactory"
  #?(:cljs (react-factory HStack)))
