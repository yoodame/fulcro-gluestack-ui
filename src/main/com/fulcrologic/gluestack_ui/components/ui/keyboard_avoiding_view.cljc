(ns com.fulcrologic.gluestack-ui.components.ui.keyboard-avoiding-view
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/keyboard-avoiding-view/index.js" :refer [KeyboardAvoidingView]])))

(def ui-keyboard-avoiding-view
  "KeyboardAvoidingView Factory"
  #?(:cljs (react-factory KeyboardAvoidingView)))
