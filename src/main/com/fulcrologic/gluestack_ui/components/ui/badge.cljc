(ns com.fulcrologic.gluestack-ui.components.ui.badge
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory ]]
    #?(:cljs ["@/components/ui/badge/index.js" :refer [Badge BadgeIcon BadgeText]])))

(def ui-badge
  "BadgeFactory"
  #?(:cljs (react-factory Badge)))

(def ui-badge-icon
  "BadgeIconFactory"
  #?(:cljs BadgeIcon))

(def ui-badge-text
  "BadgeTextFactory"
  #?(:cljs (react-factory BadgeText)))
