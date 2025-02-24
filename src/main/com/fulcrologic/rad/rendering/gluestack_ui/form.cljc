(ns com.fulcrologic.rad.rendering.gluestack-ui.form
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.gluestack-ui.components.ui.text :refer [ui-text]]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.file :as file]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.instant :as instant]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.text :as text]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.time :as time]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.timezone :as timezone]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.uuid :as uuid]
    [taoensso.timbre :as log]))

(defn render-attribute [env attr options]
  (let [{k ::attr/qualified-key} attr
        subforms (fo/subform-options options attr)]
    (if (contains? subforms k)
      (let [render-ref (or (form/ref-container-renderer env attr) #_standard-ref-container)]
        (render-ref env attr options))
      (form/render-field env attr))))

(defsc StandardFormContainer [this {::form/keys [props computed-props form-instance master-form] :as env}]
  {:shouldComponentUpdate (fn [_ _ _] true)})

(def standard-form-container (comp/factory StandardFormContainer))

(defn standard-ref-container [env {::attr/keys [cardinality] :as attr} options]
  (log/debug "standard-ref-container" env attr options)
  #_(if (= :many cardinality)
      (render-to-many env attr options)
      (render-to-one env attr options)))

(defn file-ref-container
  [env {::attr/keys [cardinality] :as attr} options]
  (if (= :many cardinality)
    ()
    #_(ui-many-files {:env env :attribute attr :options options})
    #_(ui-single-file {:env env :attribute attr :options options})))

;(def render-field (render-field-factory text-input))
