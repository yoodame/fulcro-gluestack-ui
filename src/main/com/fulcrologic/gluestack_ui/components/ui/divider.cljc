(ns com.fulcrologic.gluestack-ui.components.ui.divider
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/divider/index.js" :refer [Divider]])))

(def ui-divider
  "Divider Factory"
  #?(:cljs (react-factory Divider)))
