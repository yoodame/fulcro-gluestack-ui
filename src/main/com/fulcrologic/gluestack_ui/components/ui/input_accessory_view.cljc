(ns com.fulcrologic.gluestack-ui.components.ui.input-accessory-view
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/input-accessory-view/index.js" :refer [InputAccessoryView]])))

(def ui-input-accessory-view
  "InputAccessoryView Factory"
  #?(:cljs (react-factory InputAccessoryView)))
