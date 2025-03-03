(ns com.fulcrologic.gluestack-ui.components.ui.table
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/table/index.js" :refer [Table TableHeader TableBody TableFooter TableHead TableRow TableData TableCaption]])))

(def ui-table
  "Table Factory"
  #?(:cljs (react-factory Table)))

(def ui-table-header
  "TableHeader Factory"
  #?(:cljs (react-factory TableHeader)))

(def ui-table-body
  "TableBody Factory"
  #?(:cljs (react-factory TableBody)))

(def ui-table-footer
  "TableFooter Factory"
  #?(:cljs (react-factory TableFooter)))

(def ui-table-head
  "TableHead Factory"
  #?(:cljs (react-factory TableHead)))

(def ui-table-row
  "TableRow Factory"
  #?(:cljs (react-factory TableRow)))

(def ui-table-data
  "TableData Factory"
  #?(:cljs (react-factory TableData)))

(def ui-table-caption
  "TableCaption Factory"
  #?(:cljs (react-factory TableCaption)))
