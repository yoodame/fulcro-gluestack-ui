(ns com.fulcrologic.gluestack-ui.components.ui.slider
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/slider/index.js" :refer [Slider SliderThumb SliderTrack SliderFilledTrack UISlider]])))

(def ui-slider
  "Slider Factory"
  #?(:cljs (react-factory Slider)))

(def ui-slider-thumb
  "SliderThumb Factory"
  #?(:cljs (react-factory SliderThumb)))

(def ui-slider-track
  "SliderTrack Factory"
  #?(:cljs (react-factory SliderTrack)))

(def ui-slider-filled-track
  "SliderFilledTrack Factory"
  #?(:cljs (react-factory SliderFilledTrack)))

(def ui-u-i-slider
  "UISlider Factory"
  #?(:cljs (react-factory UISlider)))
