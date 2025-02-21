(ns com.fulcrologic.gluestack-ui.components.ui.text
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/text/index.js" :refer [Text]])))

(def ui-text
  "TextFactory"
  #?(:cljs (react-factory Text)))
