(ns com.fulcrologic.gluestack-ui.components.ui.card
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/card/index.js" :refer [Card]])))

(def ui-card
  "Card Factory"
  #?(:cljs (react-factory Card)))
