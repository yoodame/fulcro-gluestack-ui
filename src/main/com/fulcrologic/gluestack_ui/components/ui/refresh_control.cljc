(ns com.fulcrologic.gluestack-ui.components.ui.refresh-control
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/refresh-control/index.js" :refer [RefreshControl]])))

(def ui-refresh-control
  "RefreshControl Factory"
  #?(:cljs (react-factory RefreshControl)))
