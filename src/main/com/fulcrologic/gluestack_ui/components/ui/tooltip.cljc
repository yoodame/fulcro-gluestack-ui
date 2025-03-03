(ns com.fulcrologic.gluestack-ui.components.ui.tooltip
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/tooltip/index.js" :refer [Tooltip TooltipContent TooltipText UITooltip]])))

(def ui-tooltip
  "Tooltip Factory"
  #?(:cljs (react-factory Tooltip)))

(def ui-tooltip-content
  "TooltipContent Factory"
  #?(:cljs (react-factory TooltipContent)))

(def ui-tooltip-text
  "TooltipText Factory"
  #?(:cljs (react-factory TooltipText)))

(def ui-u-i-tooltip
  "UITooltip Factory"
  #?(:cljs (react-factory UITooltip)))
