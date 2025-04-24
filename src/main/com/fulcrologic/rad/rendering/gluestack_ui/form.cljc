(ns com.fulcrologic.rad.rendering.gluestack-ui.form
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.form-render-options :as fro]
    [com.fulcrologic.rad.rendering.gluestack-ui.field :as field]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control
     :refer [ui-form-control-error ui-form-control-error-text]]
    [com.fulcrologic.gluestack-ui.components.ui.vstack :refer [ui-v-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.heading :refer [ui-heading]]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.file :as file]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.instant :as instant]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.text :as text]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.time :as time]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.timezone :as timezone]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.uuid :as uuid]
    [com.fulcrologic.rad.rendering.gluestack-ui.form-options :as gufo]
    [com.fulcrologic.rad.options-util :refer [?! narrow-keyword]]
    [taoensso.timbre :as log]))

(defsc StandardFormContainer [this {::form/keys [props computed-props form-instance master-form] :as env}]
  {:shouldComponentUpdate (fn [_ _ _] true)})

(def standard-form-container (comp/factory StandardFormContainer))

;; =====================================================================================
;; REF FORM RENDERING
;; =====================================================================================

(defn render-to-one [{:keys                         [env attribute field-context]
                      {::form/keys [form-instance]} :env
                      {k ::attr/qualified-key}      :attribute}]
  ;#_#_#_{::form/keys [master-form
  ;                    form-instance] :as env} {k ::attr/qualified-key :as attr} options]

  (let [options    (comp/component-options form-instance)
        {::form/keys [ui can-add? can-delete? title ref-container-class]} (fo/subform-options options attribute)
        form-props (comp/props form-instance)
        {:keys [visible? invalid? validation-message]} field-context
        ui-props   (get form-props k)
        top-class  (or (gufo/top-class form-instance attribute) "")]

    (let [ui-factory (comp/computed-factory ui)
          ChildForm  (if (comp/union-component? ui)
                       (comp/union-child-for-props ui ui-props)
                       ui)
          title      (?! (or title (some-> ChildForm (comp/component-options ::form/title)) "") form-instance form-props)
          std-props  {::form/nested?         true
                      ::form/parent          form-instance
                      ::form/parent-relation k
                      ::form/can-delete?     (or
                                               (?! can-delete? form-instance form-props)
                                               false)}]
      (when visible?
        (ui-box {:key (str k) :className [top-class (?! ref-container-class env)]}
          (ui-v-stack {:space "xs" :className "mb-2 gap-0"}

            (when (not-empty title) (ui-heading {:size "md" :className "font-semibold"} title))

            (ui-form-control-error {}
              (ui-form-control-error-text {} validation-message)))

          (ui-factory ui-props (merge env std-props)))))))


(defn standard-ref-container [env {::attr/keys [cardinality] :as attr} options]
  (if (= :many cardinality)
    ()
    ((field/render-field-factory render-to-one) env attr)))

(defn file-ref-container
  [env {::attr/keys [cardinality] :as attr} options]
  (if (= :many cardinality)
    ()
    #_(ui-many-files {:env env :attribute attr :options options})
    #_(ui-single-file {:env env :attribute attr :options options})))

;(def render-field (render-field-factory text-input))

(defn render-attribute [env attr options]
  (cond
    (or
      (fro/fields-style attr)
      (fro/style attr)) (form/render-field env attr)
    (fo/subform-options options attr) (let [render-ref (or (form/ref-container-renderer env attr) standard-ref-container)]
                                        (render-ref env attr options))
    :else (form/render-field env attr)))
