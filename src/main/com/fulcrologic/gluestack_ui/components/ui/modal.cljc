(ns com.fulcrologic.gluestack-ui.components.ui.modal
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/modal/index.js" :refer [Modal ModalBackdrop ModalContent ModalCloseButton ModalHeader ModalBody ModalFooter]])))

(def ui-modal
  "Modal Factory"
  #?(:cljs (react-factory Modal)))

(def ui-modal-backdrop
  "ModalBackdrop Factory"
  #?(:cljs (react-factory ModalBackdrop)))

(def ui-modal-content
  "ModalContent Factory"
  #?(:cljs (react-factory ModalContent)))

(def ui-modal-close-button
  "ModalCloseButton Factory"
  #?(:cljs (react-factory ModalCloseButton)))

(def ui-modal-header
  "ModalHeader Factory"
  #?(:cljs (react-factory ModalHeader)))

(def ui-modal-body
  "ModalBody Factory"
  #?(:cljs (react-factory ModalBody)))

(def ui-modal-footer
  "ModalFooter Factory"
  #?(:cljs (react-factory ModalFooter)))
