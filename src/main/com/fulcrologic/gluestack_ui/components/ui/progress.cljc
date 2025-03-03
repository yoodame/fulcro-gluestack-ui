(ns com.fulcrologic.gluestack-ui.components.ui.progress
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/progress/index.js" :refer [Progress ProgressFilledTrack UIProgress]])))

(def ui-progress
  "Progress Factory"
  #?(:cljs (react-factory Progress)))

(def ui-progress-filled-track
  "ProgressFilledTrack Factory"
  #?(:cljs (react-factory ProgressFilledTrack)))

(def ui-u-i-progress
  "UIProgress Factory"
  #?(:cljs (react-factory UIProgress)))
