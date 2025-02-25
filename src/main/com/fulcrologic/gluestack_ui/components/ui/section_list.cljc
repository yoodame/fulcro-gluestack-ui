(ns com.fulcrologic.gluestack-ui.components.ui.section-list
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/section-list/index.js" :refer [SectionList]])))

(def ui-section-list
  "SectionList Factory"
  #?(:cljs (react-factory SectionList)))
