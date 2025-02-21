(ns com.fulcrologic.gluestack-ui.components.ui.box
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/box/index.js" :refer [Box]])))

(def ui-box
  "BoxFactory"
  #?(:cljs (react-factory Box)))
