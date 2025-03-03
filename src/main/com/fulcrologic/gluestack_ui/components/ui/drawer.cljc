(ns com.fulcrologic.gluestack-ui.components.ui.drawer
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/drawer/index.js" :refer [Drawer DrawerBackdrop DrawerContent DrawerCloseButton DrawerHeader DrawerBody DrawerFooter]])))

(def ui-drawer
  "Drawer Factory"
  #?(:cljs (react-factory Drawer)))

(def ui-drawer-backdrop
  "DrawerBackdrop Factory"
  #?(:cljs (react-factory DrawerBackdrop)))

(def ui-drawer-content
  "DrawerContent Factory"
  #?(:cljs (react-factory DrawerContent)))

(def ui-drawer-close-button
  "DrawerCloseButton Factory"
  #?(:cljs (react-factory DrawerCloseButton)))

(def ui-drawer-header
  "DrawerHeader Factory"
  #?(:cljs (react-factory DrawerHeader)))

(def ui-drawer-body
  "DrawerBody Factory"
  #?(:cljs (react-factory DrawerBody)))

(def ui-drawer-footer
  "DrawerFooter Factory"
  #?(:cljs (react-factory DrawerFooter)))
