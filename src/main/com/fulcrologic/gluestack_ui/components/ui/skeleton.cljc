(ns com.fulcrologic.gluestack-ui.components.ui.skeleton
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/skeleton/index.js" :refer [Skeleton SkeletonText]])))

(def ui-skeleton
  "SkeletonFactory"
  #?(:cljs (react-factory Skeleton)))

(def ui-skeleton-text
  "SkeletonTextFactory"
  #?(:cljs (react-factory SkeletonText)))
