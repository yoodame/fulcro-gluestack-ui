(ns com.fulcrologic.rad.rendering.gluestack-ui.form-rendering
  (:require
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.debugging :as debug]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.form-render :as fr]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.center :refer [ui-center]]
    [com.fulcrologic.gluestack-ui.components.ui.hstack :refer [ui-h-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.heading :refer [ui-heading]]
    [com.fulcrologic.gluestack-ui.components.ui.text :refer [ui-text]]
    [com.fulcrologic.gluestack-ui.components.ui.vstack :refer [ui-v-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.safe-area-view :refer [ui-safe-area-view]]
    [com.fulcrologic.rad.rendering.gluestack-ui-options :as guo]
    [com.fulcrologic.rad.rendering.gluestack-ui.form :as rgf]
    [com.fulcrologic.rad.rendering.gluestack-ui.text-field :as text-field]
    [taoensso.timbre :as log]))

(defmethod fr/render-form :default [{::form/keys [form-instance parent parent-relation master-form] :as renv} id-attr]
  "Default form container."
  (ui-box {:className "flex-1 bg-background-100" :keys (str (comp/get-ident form-instance))}
    (fr/render-header renv id-attr)
    (fr/render-fields renv id-attr)
    (fr/render-footer renv id-attr)))

(defmethod fr/render-header :default [{::form/keys [master-form form-instance] :as env} attr]
  "Default form header."
  (let [nested?         (not= master-form form-instance)
        props           (comp/props form-instance)
        computed-props  (comp/get-computed props)
        {::form/keys [title action-buttons controls show-header?]} (comp/component-options form-instance)
        title           (?! title form-instance props)
        action-buttons  (if action-buttons action-buttons form/standard-action-buttons)
        show-header?    (cond
                          (some? show-header?) (?! show-header? master-form)
                          (some? (fo/show-header? computed-props)) (?! (fo/show-header? computed-props) master-form)
                          :else true)
        {::form/keys [can-delete?]} computed-props
        read-only-form? (or
                          (?! (comp/component-options form-instance ::form/read-only?) form-instance)
                          (?! (comp/component-options master-form ::form/read-only?) master-form))

        {:ui/keys [new?] ::form/keys [errors]} props
        invalid?        (if read-only-form? false (form/invalid? env))
        errors?         (or invalid? (seq errors))]
    ;(log/info "-----fr/render-header---" )
    (if nested?
      ()
      (ui-box {:keys      (str (comp/get-ident form-instance))
               :className (or
                            (?! (guo/get-rendering-options form-instance guo/layout-class) env)
                            (?! (comp/component-options form-instance guo/layout-class) env)
                            (?! (comp/component-options form-instance ::top-level-class) env)
                            "p-4 bg-background-0")}
        (ui-safe-area-view {})
        (when show-header?
          (ui-h-stack {:space "md" :className "items-center justify-center"}
            (if (string? title)
              (ui-heading {:size "lg" :className "text-typography-900 font-semibold"} title)
              title)))))))

(defmethod fr/render-footer :default [renv attr])

(defmethod fr/render-fields :default [{::form/keys [form-instance] :as env} attr]
  (let [{::form/keys [attributes layout tabbed-layout debug?] :as options} (comp/component-options form-instance)
        layout (cond
                 (vector? layout) () #_(render-layout env options)
                 (vector? tabbed-layout) () #_(ui-tabbed-layout env options)
                 :else (ui-v-stack {:space "lg"
                                    :className "p-4"}
                        (mapv (fn [attr] (rgf/render-attribute env attr options)) attributes)))]
    (if (and #?(:clj false :cljs goog.DEBUG) debug?)
      (debug/top-bottom-debugger form-instance (comp/props form-instance)
        (constantly layout))
      layout)))

(defmethod fr/render-field [:string :default] [{::form/keys [form-instance] :as renv} field-attr]
  (text-field/render-field renv field-attr))

(defmethod fr/render-field [:string :multi-line] [{::form/keys [form-instance] :as renv} field-attr]
  (text-field/render-multi-line renv field-attr))
