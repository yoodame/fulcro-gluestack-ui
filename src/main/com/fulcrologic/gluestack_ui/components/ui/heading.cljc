(ns com.fulcrologic.gluestack-ui.components.ui.heading
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/heading/index.js" :refer [Heading]])))

(def ui-heading
  "HeadingFactory"
  #?(:cljs (react-factory Heading)))
