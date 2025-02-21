(ns com.fulcrologic.gluestack-ui.components.ui.badge
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory ]]
    #?(:cljs ["@/components/ui/badge/index.js" :refer [Badge BadgeIcon BadgeText]])))

(def ui-badge
  "React native factory for Badge"
  #?(:cljs (react-factory Badge)))

(def ui-badge-icon
  "React native factory for BadgeIcon"
  #?(:cljs BadgeIcon))

(def ui-badge-text
  "React native factory for BadgeText"
  #?(:cljs (react-factory BadgeText)))
