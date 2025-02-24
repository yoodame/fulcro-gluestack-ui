(ns com.fulcrologic.rad.rendering.gluestack-ui.text-field
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    ;#?(:cljs [com.fulcrologic.fulcro.dom :refer [div label input textarea]]
    ;   :clj  [com.fulcrologic.fulcro.dom-server :refer [div label input textarea]])
    [com.fulcrologic.fulcro.dom.events :as evt]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.input :refer [ui-input ui-input-field]]
    [com.fulcrologic.gluestack-ui.components.ui.textarea :refer [ui-textarea ui-textarea-input]]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.options-util :refer [?!]]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.components :refer [ui-wrapped-dropdown]]
    [com.fulcrologic.rad.rendering.gluestack-ui.form-options :as gufo]
    [com.fulcrologic.rad.rendering.gluestack-ui.field :refer [render-field-factory]]
    [taoensso.timbre :as log]))

;(defn- with-handlers [type {:keys [value onChange onBlur] :as props}]
;  (assoc props
;    :value (or value "")
;    :type type
;    :onBlur (fn [evt]
;              (when onBlur
;                (onBlur (evt/target-value evt))))
;    :onChange (fn [evt]
;                (when onChange
;                  (onChange (evt/target-value evt))))))

(defn- text-input [props]
  (let [input-props       (cond-> props
                            true (dissoc :placeholder :onBlur :onChangeText :value)
                            (not (:className props)) (assoc :className "my-1 bg-background-0")
                            true (assoc :type "text"))
        input-field-props (select-keys props [:placeholder :onBlur :onChangeText :value])]
    (ui-input input-props
      (ui-input-field input-field-props))))

(def render-field (render-field-factory text-input))
;(def render-password (render-field-factory password-input))
;(def render-viewable-password (render-field-factory (comp/factory ViewablePasswordField)))

(defn- textarea-input [{:keys [value onChange onBlur] :as props}]
  (let [textarea-props (cond-> props
                         true (dissoc :placeholder :onBlur :onChangeText :value)
                         (not (:className props)) (assoc :className "my-1 bg-background-0"))
        textarea-input-props (select-keys props [:placeholder :onBlur :onChangeText :value])]
    (ui-textarea textarea-props
      (ui-textarea-input textarea-input-props))))

(def render-multi-line (render-field-factory textarea-input))


