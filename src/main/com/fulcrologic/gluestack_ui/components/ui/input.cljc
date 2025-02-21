(ns com.fulcrologic.gluestack-ui.components.ui.input
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory wrap-text-input ]]
    #?(:cljs ["@/components/ui/input/index.js" :refer [Input InputField InputIcon InputSlot]])))

(def ui-input
  "React native factory for Input"
  #?(:cljs (react-factory Input)))

(def ui-input-field
  "React native factory for InputField"
  #?(:cljs (wrap-text-input InputField)))

(def ui-input-icon
  "React native factory for InputIcon"
  #?(:cljs InputIcon))

(def ui-input-slot
  "React native factory for InputSlot"
  #?(:cljs (react-factory InputSlot)))
