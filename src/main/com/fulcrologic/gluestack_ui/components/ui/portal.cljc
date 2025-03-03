(ns com.fulcrologic.gluestack-ui.components.ui.portal
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/portal/index.js" :refer [Portal]])))

(def ui-portal
  "Portal Factory"
  #?(:cljs (react-factory Portal)))
