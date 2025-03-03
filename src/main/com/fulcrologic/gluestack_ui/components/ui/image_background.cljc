(ns com.fulcrologic.gluestack-ui.components.ui.image-background
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/image-background/index.js" :refer [ImageBackground]])))

(def ui-image-background
  "ImageBackground Factory"
  #?(:cljs (react-factory ImageBackground)))
