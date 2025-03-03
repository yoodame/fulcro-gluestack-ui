(ns com.fulcrologic.gluestack-ui.components.ui.select
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/select/index.js" :refer [Select SelectTrigger SelectInput SelectIcon SelectPortal SelectBackdrop SelectContent SelectDragIndicator SelectDragIndicatorWrapper SelectItem SelectScrollView SelectVirtualizedList SelectFlatList SelectSectionList SelectSectionHeaderText]])))

(def ui-select
  "Select Factory"
  #?(:cljs (react-factory Select)))

(def ui-select-trigger
  "SelectTrigger Factory"
  #?(:cljs (react-factory SelectTrigger)))

(def ui-select-input
  "SelectInput Factory"
  #?(:cljs (react-factory SelectInput)))

(def ui-select-icon
  "SelectIcon Factory"
  #?(:cljs (react-factory SelectIcon)))

(def ui-select-portal
  "SelectPortal Factory"
  #?(:cljs (react-factory SelectPortal)))

(def ui-select-backdrop
  "SelectBackdrop Factory"
  #?(:cljs (react-factory SelectBackdrop)))

(def ui-select-content
  "SelectContent Factory"
  #?(:cljs (react-factory SelectContent)))

(def ui-select-drag-indicator
  "SelectDragIndicator Factory"
  #?(:cljs (react-factory SelectDragIndicator)))

(def ui-select-drag-indicator-wrapper
  "SelectDragIndicatorWrapper Factory"
  #?(:cljs (react-factory SelectDragIndicatorWrapper)))

(def ui-select-item
  "SelectItem Factory"
  #?(:cljs (react-factory SelectItem)))

(def ui-select-scroll-view
  "SelectScrollView Factory"
  #?(:cljs (react-factory SelectScrollView)))

(def ui-select-virtualized-list
  "SelectVirtualizedList Factory"
  #?(:cljs (react-factory SelectVirtualizedList)))

(def ui-select-flat-list
  "SelectFlatList Factory"
  #?(:cljs (react-factory SelectFlatList)))

(def ui-select-section-list
  "SelectSectionList Factory"
  #?(:cljs (react-factory SelectSectionList)))

(def ui-select-section-header-text
  "SelectSectionHeaderText Factory"
  #?(:cljs (react-factory SelectSectionHeaderText)))
