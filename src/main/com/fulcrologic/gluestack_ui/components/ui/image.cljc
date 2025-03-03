(ns com.fulcrologic.gluestack-ui.components.ui.image
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/image/index.js" :refer [Image]])))

(def ui-image
  "Image Factory"
  #?(:cljs (react-factory Image)))
