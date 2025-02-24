(ns com.fulcrologic.rad.rendering.gluestack-ui-controls
  (:require
    [com.fulcrologic.rad.rendering.gluestack-ui.form :as gui-form]))

(def all-controls
  {:com.fulcrologic.rad.form/element->style->layout
   {:form-container      {:default      gui-form/standard-form-container}
                          ;:file-as-icon gui-form/file-icon-renderer}
    ;:form-body-container {:default gui-form/standard-form-layout-renderer}
    :ref-container       {:default gui-form/standard-ref-container}}})
                          ;:file    gui-form/file-ref-container}}})

;:com.fulcrologic.rad.form/type->style->control
;{:ref {#_#_:pick-one  entity-picker/to-one-picker
;       #_#_:pick-many entity-picker/to-many-picker}}})



