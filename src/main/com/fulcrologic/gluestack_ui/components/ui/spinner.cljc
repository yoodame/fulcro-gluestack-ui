(ns com.fulcrologic.gluestack-ui.components.ui.spinner
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/spinner/index.js" :refer [Spinner]])))

(def ui-spinner
  "SpinnerFactory"
  #?(:cljs (react-factory Spinner)))
