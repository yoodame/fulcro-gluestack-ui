(ns com.fulcrologic.gluestack-ui.components.ui.actionsheet
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory ]]
    #?(:cljs ["@/components/ui/actionsheet/index.js" :refer [Actionsheet ActionsheetContent ActionsheetItem ActionsheetItemText ActionsheetDragIndicator ActionsheetDragIndicatorWrapper ActionsheetBackdrop ActionsheetScrollView ActionsheetVirtualizedList ActionsheetFlatList ActionsheetSectionList ActionsheetSectionHeaderText ActionsheetIcon]])))

(def ui-actionsheet
  "React native factory for Actionsheet"
  #?(:cljs (react-factory Actionsheet)))

(def ui-actionsheet-content
  "React native factory for ActionsheetContent"
  #?(:cljs (react-factory ActionsheetContent)))

(def ui-actionsheet-item
  "React native factory for ActionsheetItem"
  #?(:cljs (react-factory ActionsheetItem)))

(def ui-actionsheet-item-text
  "React native factory for ActionsheetItemText"
  #?(:cljs (react-factory ActionsheetItemText)))

(def ui-actionsheet-drag-indicator
  "React native factory for ActionsheetDragIndicator"
  #?(:cljs (react-factory ActionsheetDragIndicator)))

(def ui-actionsheet-drag-indicator-wrapper
  "React native factory for ActionsheetDragIndicatorWrapper"
  #?(:cljs (react-factory ActionsheetDragIndicatorWrapper)))

(def ui-actionsheet-backdrop
  "React native factory for ActionsheetBackdrop"
  #?(:cljs (react-factory ActionsheetBackdrop)))

(def ui-actionsheet-scroll-view
  "React native factory for ActionsheetScrollView"
  #?(:cljs (react-factory ActionsheetScrollView)))

(def ui-actionsheet-virtualized-list
  "React native factory for ActionsheetVirtualizedList"
  #?(:cljs (react-factory ActionsheetVirtualizedList)))

(def ui-actionsheet-flat-list
  "React native factory for ActionsheetFlatList"
  #?(:cljs (react-factory ActionsheetFlatList)))

(def ui-actionsheet-section-list
  "React native factory for ActionsheetSectionList"
  #?(:cljs (react-factory ActionsheetSectionList)))

(def ui-actionsheet-section-header-text
  "React native factory for ActionsheetSectionHeaderText"
  #?(:cljs (react-factory ActionsheetSectionHeaderText)))

(def ui-actionsheet-icon
  "React native factory for ActionsheetIcon"
  #?(:cljs ActionsheetIcon))
