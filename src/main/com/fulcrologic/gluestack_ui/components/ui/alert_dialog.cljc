(ns com.fulcrologic.gluestack-ui.components.ui.alert-dialog
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/alert-dialog/index.js" :refer [AlertDialog AlertDialogContent AlertDialogCloseButton AlertDialogHeader AlertDialogFooter AlertDialogBody AlertDialogBackdrop]])))

(def ui-alert-dialog
  "AlertDialog Factory"
  #?(:cljs (react-factory AlertDialog)))

(def ui-alert-dialog-content
  "AlertDialogContent Factory"
  #?(:cljs (react-factory AlertDialogContent)))

(def ui-alert-dialog-close-button
  "AlertDialogCloseButton Factory"
  #?(:cljs (react-factory AlertDialogCloseButton)))

(def ui-alert-dialog-header
  "AlertDialogHeader Factory"
  #?(:cljs (react-factory AlertDialogHeader)))

(def ui-alert-dialog-footer
  "AlertDialogFooter Factory"
  #?(:cljs (react-factory AlertDialogFooter)))

(def ui-alert-dialog-body
  "AlertDialogBody Factory"
  #?(:cljs (react-factory AlertDialogBody)))

(def ui-alert-dialog-backdrop
  "AlertDialogBackdrop Factory"
  #?(:cljs (react-factory AlertDialogBackdrop)))
