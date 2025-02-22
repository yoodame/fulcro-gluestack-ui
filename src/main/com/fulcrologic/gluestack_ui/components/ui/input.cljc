(ns com.fulcrologic.gluestack-ui.components.ui.input
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory wrap-text-input]]
    #?(:cljs ["@/components/ui/input/index.js" :refer [Input InputField InputIcon InputSlot]])))

(def ui-input
  "Input Factory"
  #?(:cljs (react-factory Input)))

(def ui-input-field
  "InputField Factory"
  #?(:cljs (wrap-text-input InputField)))

(def ui-input-icon
  "InputIcon Factory"
  #?(:cljs (react-factory InputIcon)))

(def ui-input-slot
  "InputSlot Factory"
  #?(:cljs (react-factory InputSlot)))
