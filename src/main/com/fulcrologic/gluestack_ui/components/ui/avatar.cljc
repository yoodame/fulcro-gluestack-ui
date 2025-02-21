(ns com.fulcrologic.gluestack-ui.components.ui.avatar
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/avatar/index.js" :refer [Avatar AvatarBadge AvatarFallbackText AvatarImage AvatarGroup]])))

(def ui-avatar
  "React native factory for Avatar"
  #?(:cljs (react-factory Avatar)))

(def ui-avatar-badge
  "React native factory for AvatarBadge"
  #?(:cljs (react-factory AvatarBadge)))

(def ui-avatar-fallback-text
  "React native factory for AvatarFallbackText"
  #?(:cljs (react-factory AvatarFallbackText {:ui-text false})))

(def ui-avatar-image
  "React native factory for AvatarImage"
  #?(:cljs (react-factory AvatarImage)))

(def ui-avatar-group
  "React native factory for AvatarGroup"
  #?(:cljs (react-factory AvatarGroup)))
