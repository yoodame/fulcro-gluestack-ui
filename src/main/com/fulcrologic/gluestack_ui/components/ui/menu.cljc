(ns com.fulcrologic.gluestack-ui.components.ui.menu
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/menu/index.js" :refer [Menu MenuItem MenuItemLabel MenuSeparator]])))

(def ui-menu
  "React native factory for Menu"
  #?(:cljs (react-factory Menu)))

(def ui-menu-item
  "React native factory for MenuItem"
  #?(:cljs (react-factory MenuItem)))

(def ui-menu-item-label
  "React native factory for MenuItemLabel"
  #?(:cljs (react-factory MenuItemLabel)))

(def ui-menu-separator
  "React native factory for MenuSeparator"
  #?(:cljs (react-factory MenuSeparator)))
