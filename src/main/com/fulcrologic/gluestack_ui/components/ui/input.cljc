(ns com.fulcrologic.gluestack-ui.components.ui.input
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory wrap-text-input ]]
    #?(:cljs ["@/components/ui/input/index.js" :refer [Input InputField InputIcon InputSlot]])))

(def ui-input
  "InputFactory"
  #?(:cljs (react-factory Input)))

(def ui-input-field
  "InputFieldFactory"
  #?(:cljs (wrap-text-input InputField)))

(def ui-input-icon
  "InputIconFactory"
  #?(:cljs InputIcon))

(def ui-input-slot
  "InputSlotFactory"
  #?(:cljs (react-factory InputSlot)))
