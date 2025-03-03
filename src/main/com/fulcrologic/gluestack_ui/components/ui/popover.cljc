(ns com.fulcrologic.gluestack-ui.components.ui.popover
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/popover/index.js" :refer [Popover PopoverBackdrop PopoverArrow PopoverCloseButton PopoverFooter PopoverHeader PopoverBody PopoverContent]])))

(def ui-popover
  "Popover Factory"
  #?(:cljs (react-factory Popover)))

(def ui-popover-backdrop
  "PopoverBackdrop Factory"
  #?(:cljs (react-factory PopoverBackdrop)))

(def ui-popover-arrow
  "PopoverArrow Factory"
  #?(:cljs (react-factory PopoverArrow)))

(def ui-popover-close-button
  "PopoverCloseButton Factory"
  #?(:cljs (react-factory PopoverCloseButton)))

(def ui-popover-footer
  "PopoverFooter Factory"
  #?(:cljs (react-factory PopoverFooter)))

(def ui-popover-header
  "PopoverHeader Factory"
  #?(:cljs (react-factory PopoverHeader)))

(def ui-popover-body
  "PopoverBody Factory"
  #?(:cljs (react-factory PopoverBody)))

(def ui-popover-content
  "PopoverContent Factory"
  #?(:cljs (react-factory PopoverContent)))
