(ns com.fulcrologic.gluestack-ui.components.ui.view
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/view/index.js" :refer [View]])))

(def ui-view
  "View Factory"
  #?(:cljs (react-factory View)))
