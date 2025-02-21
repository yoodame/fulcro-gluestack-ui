(ns com.fulcrologic.gluestack-ui.components.ui.center
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/center/index.js" :refer [Center]])))

(def ui-center
  "React native factory for Center"
  #?(:cljs (react-factory Center)))
