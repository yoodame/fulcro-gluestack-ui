(ns com.fulcrologic.gluestack-ui.components.ui.form-control
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/form-control/index.js" :refer [FormControl FormControlError FormControlErrorText FormControlErrorIcon FormControlLabel FormControlLabelText FormControlLabelAstrick FormControlHelper FormControlHelperText UIFormControl]])))

(def ui-form-control
  "FormControl Factory"
  #?(:cljs (react-factory FormControl)))

(def ui-form-control-error
  "FormControlError Factory"
  #?(:cljs (react-factory FormControlError)))

(def ui-form-control-error-text
  "FormControlErrorText Factory"
  #?(:cljs (react-factory FormControlErrorText)))

(def ui-form-control-error-icon
  "FormControlErrorIcon Factory"
  #?(:cljs (react-factory FormControlErrorIcon)))

(def ui-form-control-label
  "FormControlLabel Factory"
  #?(:cljs (react-factory FormControlLabel)))

(def ui-form-control-label-text
  "FormControlLabelText Factory"
  #?(:cljs (react-factory FormControlLabelText)))

(def ui-form-control-label-astrick
  "FormControlLabelAstrick Factory"
  #?(:cljs (react-factory FormControlLabelAstrick)))

(def ui-form-control-helper
  "FormControlHelper Factory"
  #?(:cljs (react-factory FormControlHelper)))

(def ui-form-control-helper-text
  "FormControlHelperText Factory"
  #?(:cljs (react-factory FormControlHelperText)))

(def ui-u-i-form-control
  "UIFormControl Factory"
  #?(:cljs (react-factory UIFormControl)))
