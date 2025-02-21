(ns com.fulcrologic.gluestack-ui.components.ui.actionsheet
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory ]]
    #?(:cljs ["@/components/ui/actionsheet/index.js" :refer [Actionsheet ActionsheetContent ActionsheetItem ActionsheetItemText ActionsheetDragIndicator ActionsheetDragIndicatorWrapper ActionsheetBackdrop ActionsheetScrollView ActionsheetVirtualizedList ActionsheetFlatList ActionsheetSectionList ActionsheetSectionHeaderText ActionsheetIcon]])))

(def ui-actionsheet
  "ActionsheetFactory"
  #?(:cljs (react-factory Actionsheet)))

(def ui-actionsheet-content
  "ActionsheetContentFactory"
  #?(:cljs (react-factory ActionsheetContent)))

(def ui-actionsheet-item
  "ActionsheetItemFactory"
  #?(:cljs (react-factory ActionsheetItem)))

(def ui-actionsheet-item-text
  "ActionsheetItemTextFactory"
  #?(:cljs (react-factory ActionsheetItemText)))

(def ui-actionsheet-drag-indicator
  "ActionsheetDragIndicatorFactory"
  #?(:cljs (react-factory ActionsheetDragIndicator)))

(def ui-actionsheet-drag-indicator-wrapper
  "ActionsheetDragIndicatorWrapperFactory"
  #?(:cljs (react-factory ActionsheetDragIndicatorWrapper)))

(def ui-actionsheet-backdrop
  "ActionsheetBackdropFactory"
  #?(:cljs (react-factory ActionsheetBackdrop)))

(def ui-actionsheet-scroll-view
  "ActionsheetScrollViewFactory"
  #?(:cljs (react-factory ActionsheetScrollView)))

(def ui-actionsheet-virtualized-list
  "ActionsheetVirtualizedListFactory"
  #?(:cljs (react-factory ActionsheetVirtualizedList)))

(def ui-actionsheet-flat-list
  "ActionsheetFlatListFactory"
  #?(:cljs (react-factory ActionsheetFlatList)))

(def ui-actionsheet-section-list
  "ActionsheetSectionListFactory"
  #?(:cljs (react-factory ActionsheetSectionList)))

(def ui-actionsheet-section-header-text
  "ActionsheetSectionHeaderTextFactory"
  #?(:cljs (react-factory ActionsheetSectionHeaderText)))

(def ui-actionsheet-icon
  "ActionsheetIconFactory"
  #?(:cljs ActionsheetIcon))
