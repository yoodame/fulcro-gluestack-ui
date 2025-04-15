(ns com.fulcrologic.rad.rendering.gluestack-ui-controls
  "Main namespace for Fulcro RAD GlueStack UI renderers.
   Use this namespace to install the RAD renderers in your application.
   See README.adoc for usage examples."
  (:require
    [com.fulcrologic.rad.rendering.gluestack-ui.form :as gui-form]
    [com.fulcrologic.rad.rendering.gluestack-ui.controls.action-button :as action-button]
    [com.fulcrologic.rad.rendering.gluestack-ui.controls.pickers :as picker-controls]
    [com.fulcrologic.rad.rendering.gluestack-ui.container :as gui-container]
    [com.fulcrologic.rad.rendering.gluestack-ui.text-field :as text-field]
    [com.fulcrologic.rad.rendering.gluestack-ui.boolean-field :as boolean-field]
    [com.fulcrologic.rad.rendering.gluestack-ui.int-field :as int-field]
    [com.fulcrologic.rad.rendering.gluestack-ui.enumerated-field :as enumerated-field]
    [com.fulcrologic.rad.rendering.gluestack-ui.instant-field :as instant-field]
    [com.fulcrologic.rad.rendering.gluestack-ui.decimal-field :as decimal-field]
    [com.fulcrologic.rad.rendering.gluestack-ui.currency-field :as currency-field]
    [com.fulcrologic.rad.rendering.gluestack-ui.entity-picker :as entity-picker]
    [com.fulcrologic.rad.rendering.gluestack-ui.controls.instant-inputs :as instant-inputs]
    [com.fulcrologic.rad.rendering.gluestack-ui.controls.text-input :as text-input]))

(def all-controls
  "Map of all GlueStack UI renderers for Fulcro RAD.
   Install with: (rad-app/install-ui-controls! app gui-controls/all-controls)"
  {:com.fulcrologic.rad.form/element->style->layout
   {:form-container {:default gui-form/standard-form-container}
    ;:file-as-icon gui-form/file-icon-renderer}
    ;:form-body-container {:default gui-form/standard-form-layout-renderer}
    :ref-container  {:default gui-form/standard-ref-container}}
   ;:file    gui-form/file-ref-container}}})

   :com.fulcrologic.rad.container/style->layout
   {:default gui-container/render-container-layout}

   ;; Rad control renderers
   :com.fulcrologic.rad.form/type->style->control
   {:text    {:default    text-field/render-field
              :multi-line text-field/render-multi-line}
    :string  {:default    text-field/render-field
              :multi-line text-field/render-multi-line}
    :boolean {:default boolean-field/render-field}
    :int     {:default int-field/render-field
              :picker  enumerated-field/render-field}
    :long    {:default int-field/render-field
              :picker  enumerated-field/render-field}
    :enum    {:default enumerated-field/render-field}
    :keyword {:default enumerated-field/render-field}
    ;:instant {:default            instant-field/render-field
    ;          :date-at-noon       instant-field/render-date-at-noon-field
    ;          :midnight-on-date   instant-field/render-midnight-on-date-field
    ;          :midnight-next-date instant-field/render-midnight-next-date-field
    ;          :time-only          instant-field/render-time-only-field}
    :decimal {:default decimal-field/render-field
              :usd     currency-field/render-field}
    :ref     {:pick-one  entity-picker/to-one-picker
              :pick-many entity-picker/to-many-picker}}

   :com.fulcrologic.rad.control/type->style->control
   {:button  {:default action-button/render-control
              :link    action-button/render-control
              :outline action-button/render-control}
    :instant {:default            instant-inputs/date-time-control
              :date-at-noon       instant-inputs/date-at-noon-control
              :midnight-on-date   instant-inputs/midnight-on-date-control
              :midnight-next-date instant-inputs/midnight-next-date-control
              :time-only          instant-inputs/time-only-control}
    :picker  {:default picker-controls/render-control}
    :string  {:default text-input/render-control
              :search  text-input/render-control}}})
