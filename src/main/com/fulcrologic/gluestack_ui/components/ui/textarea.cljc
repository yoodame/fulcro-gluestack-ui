(ns com.fulcrologic.gluestack-ui.components.ui.textarea
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/textarea/index.js" :refer [Textarea TextareaInput]])))

(def ui-textarea
  "Textarea Factory"
  #?(:cljs (react-factory Textarea)))

(def ui-textarea-input
  "TextareaInput Factory"
  #?(:cljs (react-factory TextareaInput)))
