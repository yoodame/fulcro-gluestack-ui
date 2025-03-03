(ns com.fulcrologic.gluestack-ui.components.ui.switch
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/switch/index.js" :refer [Switch]])))

(def ui-switch
  "Switch Factory"
  #?(:cljs (react-factory Switch)))
