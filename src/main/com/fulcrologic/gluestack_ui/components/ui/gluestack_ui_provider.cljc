(ns com.fulcrologic.gluestack-ui.components.ui.gluestack-ui-provider
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/gluestack-ui-provider/index.js" :refer [GluestackUIProvider]])))

(def ui-gluestack-u-i-provider
  "GluestackUIProvider Factory"
  #?(:cljs (react-factory GluestackUIProvider)))
