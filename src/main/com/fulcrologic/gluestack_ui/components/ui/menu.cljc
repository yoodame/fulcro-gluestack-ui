(ns com.fulcrologic.gluestack-ui.components.ui.menu
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/menu/index.js" :refer [Menu MenuItem MenuItemLabel MenuSeparator]])))

(def ui-menu
  "MenuFactory"
  #?(:cljs (react-factory Menu)))

(def ui-menu-item
  "MenuItemFactory"
  #?(:cljs (react-factory MenuItem)))

(def ui-menu-item-label
  "MenuItemLabelFactory"
  #?(:cljs (react-factory MenuItemLabel)))

(def ui-menu-separator
  "MenuSeparatorFactory"
  #?(:cljs (react-factory MenuSeparator)))
