(ns com.fulcrologic.gluestack-ui.components.ui.toast
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [ react-factory]]
    #?(:cljs ["@/components/ui/toast/index.js" :refer [useToast Toast ToastTitle ToastDescription]])))

(def ui-use-toast
  "useToast Factory"
  #?(:cljs useToast))

(def ui-toast
  "Toast Factory"
  #?(:cljs (react-factory Toast)))

(def ui-toast-title
  "ToastTitle Factory"
  #?(:cljs (react-factory ToastTitle)))

(def ui-toast-description
  "ToastDescription Factory"
  #?(:cljs (react-factory ToastDescription)))
