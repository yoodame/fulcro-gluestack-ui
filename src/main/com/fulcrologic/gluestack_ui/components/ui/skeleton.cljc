(ns com.fulcrologic.gluestack-ui.components.ui.skeleton
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/skeleton/index.js" :refer [Skeleton SkeletonText]])))

(def ui-skeleton
  "React native factory for Skeleton"
  #?(:cljs (react-factory Skeleton)))

(def ui-skeleton-text
  "React native factory for SkeletonText"
  #?(:cljs (react-factory SkeletonText)))
