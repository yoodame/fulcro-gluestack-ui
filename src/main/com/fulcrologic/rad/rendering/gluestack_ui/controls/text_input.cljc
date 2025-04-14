(ns com.fulcrologic.rad.rendering.gluestack-ui.controls.text-input
  "Text input controls for Fulcro RAD using GlueStack UI.
   Provides standard text and search input controls for report filters and forms."
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.gluestack-ui.components.ui.input :refer [ui-input ui-input-slot ui-input-icon ui-input-field]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control :refer [ui-form-control ui-form-control-label
                                                                     ui-form-control-label-text]]
    [com.fulcrologic.gluestack-ui.components.ui.pressable :refer [ui-pressable]]
    [com.fulcrologic.gluestack-ui.lucide-icons :refer [x-icon]]
    [taoensso.timbre :as log]))

(defn- internal-store-name [control-key]
  (keyword (str 'com.fulcrologic.rad.rendering.gluestack-ui.controls.text-input_ (namespace control-key))
    (name control-key)))

(defsc TextControl [this {:keys [instance control-key style] :as control-props}]
  {:shouldComponentUpdate (fn [_ _ _] true)}
  (let [controls (control/component-controls instance)
        {:keys [label onChange placeholder icon disabled? visible? user-props] :as control}
        (get controls control-key)
        search?  (= style :search)]
    (when control
      (let [label          (?! label instance)
            disabled?      (?! disabled? instance)
            placeholder    (?! placeholder instance)
            visible?       (or (nil? visible?) (?! visible? instance))
            value          (or (control/current-value instance control-key) "")
            {:keys [last-sent-value]} (control/current-value instance (internal-store-name control-key))
            handle-change! #(control/set-parameter! instance control-key %)
            handle-run!    (fn [run-if-unchanged? _]
                             (let [actually-changed? (not= value last-sent-value)]
                               (when (and onChange (or run-if-unchanged? actually-changed?))
                                 (control/set-parameter! instance control-key value)
                                 (control/set-parameter! instance
                                   (internal-store-name control-key)
                                   {:last-sent-value value})
                                 ;; Change the URL parameter
                                 (onChange instance value))))
            handle-clear   (fn []
                             (handle-change! ""))]

        (when visible?
          (ui-form-control {:key (str control-key) :size "sm"}
            (when label
              (ui-form-control-label {}
                (ui-form-control-label-text {} label)))

            (ui-input (merge
                        {:size "sm"}
                        user-props
                        {:isDisabled disabled?
                         :className  "bg-background-50 border-background-100"})

              ;; Left icon slot (search icon or custom icon)
              (when icon
                (ui-input-slot {:className "ml-2"}
                  (ui-input-icon {:as icon :size 16})))

              ;; Text input field
              (ui-input-field {:placeholder  (or placeholder (if search? "Search..." "Enter text..."))
                               :onChangeText handle-change!
                               :inputMode    (if search? "search" "text")
                               :onBlur       (partial handle-run! false)
                               :onKeyPress   (fn [evt] (handle-run! true evt))})

              ;; Clear button (X) - only shown for search style when there's content
              (when (not= value "")
                (ui-input-slot {:className "mr-2"}
                  (ui-pressable {:onPress   handle-clear
                                 :disabled  disabled?
                                 :className "p-1"}
                    (ui-input-icon {:as        x-icon
                                    :size      16
                                    :className "text-background-800"})))))))))))


(def ui-text-control (comp/factory TextControl {:keyfn :control-key}))

(defn render-control
  "Render a text input control component with the given control environment.
   Can be used for both :default and :search styles."
  [render-env]
  (ui-text-control render-env))
