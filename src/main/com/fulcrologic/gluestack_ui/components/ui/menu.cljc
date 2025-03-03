(ns com.fulcrologic.gluestack-ui.components.ui.menu
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/menu/index.js" :refer [Menu MenuItem MenuItemLabel MenuSeparator UIMenu]])))

(def ui-menu
  "Menu Factory"
  #?(:cljs (react-factory Menu)))

(def ui-menu-item
  "MenuItem Factory"
  #?(:cljs (react-factory MenuItem)))

(def ui-menu-item-label
  "MenuItemLabel Factory"
  #?(:cljs (react-factory MenuItemLabel)))

(def ui-menu-separator
  "MenuSeparator Factory"
  #?(:cljs (react-factory MenuSeparator)))

(def ui-u-i-menu
  "UIMenu Factory"
  #?(:cljs (react-factory UIMenu)))
