(ns com.fulcrologic.gluestack-ui.components.ui.badge
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/badge/index.js" :refer [Badge BadgeIcon BadgeText]])))

(def ui-badge
  "Badge Factory"
  #?(:cljs (react-factory Badge)))

(def ui-badge-icon
  "BadgeIcon Factory"
  #?(:cljs (react-factory BadgeIcon)))

(def ui-badge-text
  "BadgeText Factory"
  #?(:cljs (react-factory BadgeText)))
