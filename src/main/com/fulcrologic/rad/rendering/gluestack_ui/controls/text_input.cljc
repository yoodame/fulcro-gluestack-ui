(ns com.fulcrologic.rad.rendering.gluestack-ui.controls.text-input
  "Text input controls for Fulcro RAD using GlueStack UI.
   Provides standard text and search input controls for report filters and forms."
  (:require
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.rad.options-util :refer [?!]]
    [com.fulcrologic.gluestack-ui.components.ui.input :refer [ui-input ui-input-slot ui-input-icon ui-input-field]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control :refer [ui-form-control ui-form-control-label
                                                                     ui-form-control-label-text]]))

(defn- internal-store-name [control-key]
  (keyword (str 'com.fulcrologic.rad.rendering.gluestack-ui.controls.text-input_ (namespace control-key))
    (name control-key)))

(defsc TextControl [this {:keys [instance control-key style] :as control-props}]
  {:shouldComponentUpdate (fn [_ _ _] true)}
  (let [controls (control/component-controls instance)
        {:keys [label onChange placeholder icon onIconClick disabled? visible? user-props] :as control}
        (get controls control-key)
        search?  (= style :search)]
    (when control
      (let [label         (?! label instance)
            disabled?     (?! disabled? instance)
            placeholder   (?! placeholder instance)
            visible?      (or (nil? visible?) (?! visible? instance))
            value         (or (control/current-value instance control-key) "")
            {:keys [last-sent-value]} (control/current-value instance (internal-store-name control-key))

            run-change!   (fn [new-value run-if-unchanged?]
                            (let [actually-changed? (not= new-value last-sent-value)]
                              (when (and onChange (or run-if-unchanged? actually-changed?))
                                (control/set-parameter! instance control-key new-value)
                                (control/set-parameter! instance
                                  (internal-store-name control-key)
                                  {:last-sent-value new-value})
                                ;; Change the URL parameter
                                (onChange instance new-value))))

            handle-change (fn [text]
                            (control/set-parameter! instance control-key text)
                            ;; For search style, run onChange immediately
                            (when search?
                              (run-change! text true)))

            handle-submit (fn [_]
                            (run-change! value true))

            handle-blur   (fn []
                            (run-change! value false))]

        (when visible?
          (ui-form-control {:key (str control-key) :size "sm"}
            (when label
              (ui-form-control-label {}
                (ui-form-control-label-text {} label)))

            (ui-input (merge
                        {:size "sm"}
                        user-props
                        {:isDisabled disabled?
                         :className  "bg-background-50"})
              (when icon (ui-input-slot {:className "ml-2"}
                           (ui-input-icon {:as icon :size 16})))
              (ui-input-field {:value           value
                               :placeholder     (or placeholder (if search? "Search..." "Enter text..."))
                               :onChangeText    handle-change
                               :onBlur          handle-blur
                               :onSubmitEditing handle-submit
                               }))))))))

(def ui-text-control (comp/factory TextControl {:keyfn :control-key}))

(defn render-control
  "Render a text input control component with the given control environment.
   Can be used for both :default and :search styles."
  [render-env]
  (ui-text-control render-env))
