(ns com.fulcrologic.rad.rendering.gluestack-ui.form-rendering
  (:require
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.rad.debugging :as debug]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.form-render :as fr]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.vstack :refer [ui-v-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.scroll-view :refer [ui-scroll-view]]
    [com.fulcrologic.gluestack-ui.components.ui.safe-area-view :refer [ui-safe-area-view]]
    [com.fulcrologic.rad.gluestack-ui-options :as guo]
    [com.fulcrologic.rad.rendering.gluestack-ui.form :as gui-form]
    [com.fulcrologic.rad.rendering.gluestack-ui.form :as rgf]
    [com.fulcrologic.rad.rendering.gluestack-ui.text-field :as text-field]
    [taoensso.timbre :as log]))

(defmethod fr/render-form :default [{::form/keys [form-instance parent parent-relation master-form] :as renv} id-attr]
  "Default form container."
  (let [nested? (not= master-form form-instance)
        ui-body (if nested? ui-box ui-scroll-view)]
    (ui-box {:className [(when-not nested? "flex-1 bg-background-100")] :keys (str (comp/get-ident form-instance))}
      (fr/render-header renv id-attr)
      (ui-body {}
        (ui-v-stack {:space "lg" :className [(when-not nested? "p-4")]}
          (fr/render-fields renv id-attr))
        (fr/render-footer renv id-attr)))))

(defmethod fr/render-header :default [{::form/keys [master-form form-instance] :as env} attr]
  "Default form header."
  (gui-form/standard-form-header env attr))

(defmethod fr/render-footer :default [renv attr])

(defmethod fr/render-fields :default [{::form/keys [form-instance] :as env} attr]
  (gui-form/standard-form-layout-container env))

(defmethod fr/render-field [:string :default] [{::form/keys [form-instance] :as renv} field-attr]
  (text-field/render-field renv field-attr))

(defmethod fr/render-field [:string :multi-line] [{::form/keys [form-instance] :as renv} field-attr]
  (text-field/render-multi-line renv field-attr))

(defmethod fr/render-field [:ref :default] [{::form/keys [form-instance] :as renv} field-attr]
  (gui-form/standard-ref-container renv field-attr (comp/component-options form-instance)))
