(ns com.fulcrologic.gluestack-ui.components.ui.avatar
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/avatar/index.js" :refer [Avatar AvatarBadge AvatarFallbackText AvatarImage AvatarGroup]])))

(def ui-avatar
  "Avatar Factory"
  #?(:cljs (react-factory Avatar)))

(def ui-avatar-badge
  "AvatarBadge Factory"
  #?(:cljs (react-factory AvatarBadge)))

(def ui-avatar-fallback-text
  "AvatarFallbackText Factory"
  #?(:cljs (react-factory AvatarFallbackText {:ui-text false})))

(def ui-avatar-image
  "AvatarImage Factory"
  #?(:cljs (react-factory AvatarImage)))

(def ui-avatar-group
  "AvatarGroup Factory"
  #?(:cljs (react-factory AvatarGroup)))
