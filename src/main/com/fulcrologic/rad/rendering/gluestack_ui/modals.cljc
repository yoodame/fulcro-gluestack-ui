(ns com.fulcrologic.rad.rendering.gluestack-ui.modals
  "Modal components for Fulcro RAD forms using GlueStack UI.
   Provides reusable modal dialogs for editing entities."
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro-i18n.i18n :refer [tr]]
    [com.fulcrologic.fulcro.raw.components :as rc]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.hstack :refer [ui-h-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.text :refer [ui-text]]
    [com.fulcrologic.gluestack-ui.components.ui.button :refer [ui-button ui-button-text]]
    [com.fulcrologic.gluestack-ui.components.ui.modal :refer [ui-modal ui-modal-content ui-modal-body
                                                              ui-modal-header ui-modal-footer ui-modal-backdrop]]))

(defsc FormModal [this {:keys [Form save-mutation save-params cancel-mutation cancel-params id]}]
  {}
  (let [FormClass (comp/registry-key->class Form)]
    (ui-modal {:isOpen true}
      (ui-modal-backdrop {})
      (ui-modal-content {:width "90%" :maxWidth "800px"}
        (ui-modal-header {}
          (ui-text {:size "lg" :fontWeight "600"}
            (str (tr "Edit") " " (or (some-> FormClass comp/component-options ::form/title) ""))))
        (ui-modal-body {}
          (ui-box {}
            (when Form
              ((comp/computed-factory FormClass)
               {:form/id id}
               {:modal-form? true}))))
        (ui-modal-footer {}
          (ui-h-stack {:space "md" :justifyContent "flex-end"}
            (ui-button {:action  "secondary"
                        :onPress #(comp/transact! this [(cancel-mutation cancel-params)])}
              (ui-button-text {} (tr "Cancel")))
            (ui-button {:action  "primary"
                        :onPress #(comp/transact! this [(save-mutation save-params)])}
              (ui-button-text {} (tr "Save")))))))))

(def ui-form-modal (comp/factory FormModal))
