(ns com.fulcrologic.gluestack-ui.components.ui.link
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/link/index.js" :refer [Link LinkText UILink]])))

(def ui-link
  "Link Factory"
  #?(:cljs (react-factory Link)))

(def ui-link-text
  "LinkText Factory"
  #?(:cljs (react-factory LinkText)))

(def ui-u-i-link
  "UILink Factory"
  #?(:cljs (react-factory UILink)))
