(ns com.fulcrologic.gluestack-ui.components.ui.actionsheet
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/actionsheet/index.js" :refer [Actionsheet ActionsheetContent ActionsheetItem ActionsheetItemText ActionsheetDragIndicator ActionsheetDragIndicatorWrapper ActionsheetBackdrop ActionsheetScrollView ActionsheetVirtualizedList ActionsheetFlatList ActionsheetSectionList ActionsheetSectionHeaderText ActionsheetIcon UIActionsheet]])))

(def ui-actionsheet
  "Actionsheet Factory"
  #?(:cljs (react-factory Actionsheet)))

(def ui-actionsheet-content
  "ActionsheetContent Factory"
  #?(:cljs (react-factory ActionsheetContent)))

(def ui-actionsheet-item
  "ActionsheetItem Factory"
  #?(:cljs (react-factory ActionsheetItem)))

(def ui-actionsheet-item-text
  "ActionsheetItemText Factory"
  #?(:cljs (react-factory ActionsheetItemText)))

(def ui-actionsheet-drag-indicator
  "ActionsheetDragIndicator Factory"
  #?(:cljs (react-factory ActionsheetDragIndicator)))

(def ui-actionsheet-drag-indicator-wrapper
  "ActionsheetDragIndicatorWrapper Factory"
  #?(:cljs (react-factory ActionsheetDragIndicatorWrapper)))

(def ui-actionsheet-backdrop
  "ActionsheetBackdrop Factory"
  #?(:cljs (react-factory ActionsheetBackdrop)))

(def ui-actionsheet-scroll-view
  "ActionsheetScrollView Factory"
  #?(:cljs (react-factory ActionsheetScrollView)))

(def ui-actionsheet-virtualized-list
  "ActionsheetVirtualizedList Factory"
  #?(:cljs (react-factory ActionsheetVirtualizedList)))

(def ui-actionsheet-flat-list
  "ActionsheetFlatList Factory"
  #?(:cljs (react-factory ActionsheetFlatList)))

(def ui-actionsheet-section-list
  "ActionsheetSectionList Factory"
  #?(:cljs (react-factory ActionsheetSectionList)))

(def ui-actionsheet-section-header-text
  "ActionsheetSectionHeaderText Factory"
  #?(:cljs (react-factory ActionsheetSectionHeaderText)))

(def ui-actionsheet-icon
  "ActionsheetIcon Factory"
  #?(:cljs (react-factory ActionsheetIcon)))

(def ui-u-i-actionsheet
  "UIActionsheet Factory"
  #?(:cljs (react-factory UIActionsheet)))
