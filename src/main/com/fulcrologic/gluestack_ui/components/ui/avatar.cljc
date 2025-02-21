(ns com.fulcrologic.gluestack-ui.components.ui.avatar
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/avatar/index.js" :refer [Avatar AvatarBadge AvatarFallbackText AvatarImage AvatarGroup]])))

(def ui-avatar
  "AvatarFactory"
  #?(:cljs (react-factory Avatar)))

(def ui-avatar-badge
  "AvatarBadgeFactory"
  #?(:cljs (react-factory AvatarBadge)))

(def ui-avatar-fallback-text
  "AvatarFallbackTextFactory"
  #?(:cljs (react-factory AvatarFallbackText {:ui-text false})))

(def ui-avatar-image
  "AvatarImageFactory"
  #?(:cljs (react-factory AvatarImage)))

(def ui-avatar-group
  "AvatarGroupFactory"
  #?(:cljs (react-factory AvatarGroup)))
