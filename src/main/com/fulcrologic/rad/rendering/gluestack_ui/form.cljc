(ns com.fulcrologic.rad.rendering.gluestack-ui.form
  (:require
    [com.fulcrologic.fulcro-i18n.i18n :refer [tr trf trc]]
    [com.fulcrologic.fulcro.algorithms.form-state :as fs]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]
    [com.fulcrologic.fulcro.algorithms.tempid :as tempid]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.react.hooks :as hooks]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.rad.debugging :as debug]
    [com.fulcrologic.rad.blob :as blob]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.form-render-options :as fro]
    [com.fulcrologic.rad.gluestack-ui-options :as guo]
    [com.fulcrologic.rad.rendering.gluestack-ui.field :as field]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.form-control
     :refer [ui-form-control-error ui-form-control-error-text]]
    [com.fulcrologic.gluestack-ui.lucide-icons :refer [camera-icon file-icon image-icon]]
    [com.fulcrologic.gluestack-ui.components.ui.card :refer [ui-card]]
    [com.fulcrologic.gluestack-ui.components.ui.vstack :refer [ui-v-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.heading :refer [ui-heading]]
    [com.fulcrologic.gluestack-ui.components.ui.safe-area-view :refer [ui-safe-area-view]]
    [com.fulcrologic.gluestack-ui.components.ui.button :refer [ui-button-group ui-button ui-button-spinner ui-button-text ui-button-icon]]
    [com.fulcrologic.gluestack-ui.components.ui.hstack :refer [ui-h-stack]]
    [com.fulcrologic.gluestack-ui.components.ui.text :refer [ui-text]]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.file :as file]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.instant :as instant]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.text :as text]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.time :as time]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.timezone :as timezone]
    ;[com.fulcrologic.rad.rendering.gluestack-ui.ui-controls.uuid :as uuid]
    [com.fulcrologic.rad.rendering.gluestack-ui.form-options :as gufo]
    [com.fulcrologic.rad.options-util :refer [?! narrow-keyword]]
    [taoensso.encore :as enc]
    [taoensso.timbre :as log]))

;; =====================================================================================
;; FORM FILE REF RENDERING
;; =====================================================================================

(defsc ImagePickerButton [_ props computed-props]
  {:shouldComponentUpdate (fn [_ _ _] true)
   :use-hooks? true}
  #?(:cljs
     (let [on-selected (or (:on-selected computed-props) (fn [_] ()))
           use-camera? (or (:use-camera computed-props) false)
           picker (js/require "expo-image-picker")
           linking (js/require "expo-linking")
           options (clj->js (merge
                              {:allowsEditing false
                               :allowsMultipleSelection false
                               :legacy true
                               :mediaTypes ["images" "livePhotos" "videos"]
                               :exif true}
                              (get props :picker-options {})))
           [status requestPermission] (if use-camera?
                                        (.useCameraPermissions picker)
                                        (.useMediaLibraryPermissions picker))
           handlePicker (hooks/use-callback
                          (fn []
                            (let [launcher (if use-camera?
                                             (.launchCameraAsync picker options)
                                             (.launchImageLibraryAsync picker options))]
                              (-> launcher
                                (.then (fn [result]
                                         (when-not (aget result "canceled")
                                           (on-selected (aget result "assets"))))))))
                          [])
           handlePermission (hooks/use-callback
                              (fn []
                                (when status
                                  (let [openSettings? (and (= (aget status "status") (aget picker "PermissionStatus" "DENIED"))
                                                        (not (aget status "canAskAgain")))]
                                    (if openSettings?
                                      (.openSettings linking)
                                      (-> (requestPermission)
                                        (.then (fn [result]
                                                 (when (aget result "granted")
                                                   (handlePicker)))))))))
                              [status handlePicker requestPermission])]

       (ui-button {:size "sm" :variant "link" :onPress #(handlePermission)}
         (ui-button-icon {:as (if use-camera? camera-icon image-icon)})
         (ui-button-text {:size "md"}
           (if use-camera? "Camera" "Photo Library"))))))

(def ui-image-picker (comp/computed-factory ImagePickerButton))

(defsc DocumentPickerButton [_ props computed-props]
  {:shouldComponentUpdate (fn [_ _ _] true)
   :use-hooks? true}
  #?(:cljs
     (let [on-selected (or (:on-selected computed-props) (fn [_] ()))
           picker (js/require "expo-document-picker")
           options (clj->js (merge
                              {:copyToCacheDirectory true :multiple false}
                              (get props :picker-options {})))
           handler (fn [_]
                     (-> (.getDocumentAsync picker options)
                       (.then (fn [result]
                                (when-not (aget result "granted")
                                  (on-selected (aget result "assets")))))))]
       (ui-button {:size "sm" :variant "link" :onPress handler}
         (ui-button-icon {:as file-icon})
         (ui-button-text {:size "md"} "Browse Files")))))

(def ui-document-picker (comp/computed-factory DocumentPickerButton))

(defsc StandardFileRef [this
                        {{::form/keys [form-instance master-form] :as env} :env
                         {k ::attr/qualified-key :as attr}                 :attribute
                         {:keys [visible? read-only?] :as field-context}   :field-context
                         options                                           :options}]
  {:initLocalState (fn [this] {:input-key (str (rand-int 1000000))})
   :use-hooks? true}
  (let [{::gufo/keys [add-position]
         ::form/keys [ui title can-delete? can-add? sort-children] :as sub-options} (fo/subform-options options attr)
        form-props (comp/props form-instance)
        add?       (if read-only? false (?! can-add? form-instance attr))
        delete?    (if read-only? false (fn [item] (?! can-delete? form-instance item)))
        items      (if (attr/to-many? attr)
                     (-> form-instance comp/props k
                       (cond->
                         sort-children sort-children))
                     [(get form-props k)])
        ui-factory (comp/computed-factory ui {:keyfn (fn [item] (-> ui (comp/get-ident item) second str))})
        top-class  (gufo/top-class form-instance attr)
        title      (?! (or title (some-> ui (comp/component-options ::form/title)) "") form-instance form-props)
        file-upload-handler (fn [files]
                              (tap> files)
                              (let [new-id     (tempid/tempid)
                                    js-file    (-> files first)
                                    attributes (comp/component-options ui ::form/attributes)
                                    id-attr    (comp/component-options ui ::form/id)
                                    id-key     (::attr/qualified-key id-attr)
                                    {::attr/keys [qualified-key] :as sha-attr} (first (filter ::blob/store
                                                                                        attributes))
                                    target     (conj (comp/get-ident form-instance) k)
                                    new-entity (fs/add-form-config ui
                                                 {id-key        new-id
                                                  qualified-key ""})]
                                (merge/merge-component! form-instance ui new-entity :replace target)
                                (blob/upload-file! form-instance sha-attr js-file {:file-ident [id-key new-id]})
                                (comp/set-state! this {:input-key (str (rand-int 1000000))})))
        add        (when (or (nil? add?) add?)
                     (ui-v-stack {;; trick: changing the key on change clears the input, so a failed upload can be retried
                                  :key      (comp/get-state this :input-key)
                                  :space "xl"}
                       (ui-box {}
                         (ui-heading {:size "md"} "Upload file")
                         (ui-text {:size "sm" :className "text-typography-500"}
                           "Select one of the options below to add your file"))
                       (ui-button-group {:flexDirection "row" :space "xl"}
                         (ui-image-picker {} {:on-selected file-upload-handler :use-camera true})
                         (ui-image-picker {} {:on-selected file-upload-handler})
                         (ui-document-picker {} {:on-selected file-upload-handler}))))]


    (when visible?
      (ui-card {:size "md" :variant "outline" :className ["bg-background-0" top-class]}
        (when (not-empty title) (ui-heading {:size "md" :className "font-semibold"} title))

        (when (or (nil? add-position) (= :top add-position)) add)

        (if (seq items)
          (mapv
            (fn [props]
              (when props
                (ui-factory props
                  (merge
                    env
                    {::form/parent          form-instance
                     ::form/parent-relation k
                     ::form/can-delete?     (or (?! delete? props) false)}))))
            items)
          (ui-text {}
            (trc "there are no files in a list of uploads" "No files.")))

        (when (= :bottom add-position) add)))))

(def ui-standard-file-ref (comp/factory StandardFileRef {:keyfn (fn [{:keys [attribute]}]
                                                                  (::attr/qualified-key attribute))}))

(defn file-ref-container
  [env attr options]
  ((field/render-field-factory ui-standard-file-ref) env attr))

;; =====================================================================================
;; REF FORM RENDERING
;; =====================================================================================

(defn ui-render-to-one-ref [{:keys                         [env attribute field-context]
                             {::form/keys [form-instance]} :env
                             {k ::attr/qualified-key}      :attribute
                             options                       :options}]

  (let [{::form/keys [ui can-add? can-delete? title ref-container-class]} (fo/subform-options options attribute)
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
    (log/error "Unsupported render :many ref attributes with standard-ref-container")
    ((field/render-field-factory ui-render-to-one-ref) env attr)))

;(def render-field (render-field-factory text-input))

;; =====================================================================================
;; FORM LAYOUT RENDERING
;; =====================================================================================

(def n-fields-string {1 "one field"
                      2 "two fields"
                      3 "three fields"
                      4 "four fields"
                      5 "five fields"
                      6 "six fields"
                      7 "seven fields"})

(defn render-attribute [env attr options]
  (cond
    (or
      (fro/fields-style attr)
      (fro/style attr)) (form/render-field env attr)
    (fo/subform-options options attr) (let [render-ref (or (form/ref-container-renderer env attr) standard-ref-container)]
                                        (render-ref env attr options))
    :else (form/render-field env attr)))

(def attribute-map (memoize
                     (fn [attributes]
                       (reduce
                         (fn [m {::attr/keys [qualified-key] :as attr}]
                           (assoc m qualified-key attr))
                         {}
                         attributes))))

(defn- render-layout* [env options k->attribute layout]
  (when #?(:clj true :cljs goog.DEBUG)
    (when-not (and (vector? layout) (every? vector? layout))
      (log/error "::form/layout must be a vector of vectors!")))
  (try
    (into []
      (map-indexed
        (fn [idx row]
          (ui-box {:key idx :className (n-fields-string (count row))}
            (mapv (fn [col]
                    (enc/if-let [_    k->attribute
                                 attr (k->attribute col)]
                      (render-attribute env attr options)
                      (if (some-> options ::control/controls (get col))
                        (control/render-control (::form/form-instance env) col)
                        (log/error "Missing attribute (or lookup) for" col))))
              row)))
        layout))
    (catch #?(:clj Exception :cljs :default) _)))

(defn render-layout [env {::form/keys [attributes layout] :as options}]
  (let [k->attribute (attribute-map attributes)]
    (render-layout* env options k->attribute layout)))

(defsc TabbedLayout [this env {::form/keys [attributes tabbed-layout] :as options}]
  {:initLocalState (fn [this]
                     (try
                       {:current-tab 0
                        :tab-details (memoize
                                       (fn [attributes tabbed-layout]
                                         (let [k->attr           (attribute-map attributes)
                                               tab-labels        (filterv string? tabbed-layout)
                                               tab-label->layout (into {}
                                                                   (map vec)
                                                                   (partition 2 (mapv first (partition-by string? tabbed-layout))))]
                                           {:k->attr           k->attr
                                            :tab-labels        tab-labels
                                            :tab-label->layout tab-label->layout})))}
                       (catch #?(:clj Exception :cljs :default) _
                         (log/error "Cannot build tabs for tabbed layout. Check your tabbed-layout options for" (comp/component-name this)))))}
  #_(let [{:keys [tab-details current-tab]} (comp/get-state this)
          {:keys [k->attr tab-labels tab-label->layout]} (tab-details attributes tabbed-layout)
          active-layout (some->> current-tab
                          (get tab-labels)
                          (get tab-label->layout))]
      (div {:key (str current-tab)}
        (div :.ui.pointing.menu {}
          (map-indexed
            (fn [idx title]
              (dom/a :.item
                {:key     (str idx)
                 :onClick #(comp/set-state! this {:current-tab idx})
                 :classes [(when (= current-tab idx) "active")]}
                title)) tab-labels))
        (div :.ui.segment
          (render-layout* env options k->attr active-layout))))
  (log/error "No official tabbed-layout support for" (comp/component-name this)))

(def ui-tabbed-layout (comp/computed-factory TabbedLayout))

(defn standard-form-layout-container [{::form/keys [form-instance] :as env}]
  (let [{::form/keys [attributes layout tabbed-layout debug?] :as options} (comp/component-options form-instance)
        layout (cond
                 (vector? layout) (render-layout env options)
                 (vector? tabbed-layout) (ui-tabbed-layout env options)
                 :else (mapv (fn [attr] (render-attribute env attr options)) attributes))]
    (if (and #?(:clj false :cljs goog.DEBUG) debug?)
      (debug/top-bottom-debugger form-instance (comp/props form-instance)
        (constantly layout))
      layout)))

;; =====================================================================================
;; FORM RENDERING
;; =====================================================================================

(defn standard-form-header [{::form/keys [master-form form-instance nested?] :as env} attr]
  (let [props           (comp/props form-instance)
        computed-props  (comp/get-computed props)
        {::form/keys [can-delete?]} computed-props
        nested?         (or nested? (not= master-form form-instance))
        {::form/keys [title action-buttons controls show-header?]} (comp/component-options form-instance)
        title           (?! title form-instance props)
        action-buttons  (if action-buttons action-buttons form/standard-action-buttons)
        done-action     (::form/done (set action-buttons))
        rest-actions    (filterv #(not= % ::form/done) action-buttons)
        show-header?    (cond
                          (some? show-header?) (?! show-header? master-form)
                          (some? (fo/show-header? computed-props)) (?! (fo/show-header? computed-props) master-form)
                          :else true)
        read-only-form? (or
                          (?! (comp/component-options form-instance ::form/read-only?) form-instance)
                          (?! (comp/component-options master-form ::form/read-only?) master-form))

        {:ui/keys [new?] ::form/keys [errors]} props
        invalid?        (if read-only-form? false (form/invalid? env))
        errors?         (or invalid? (seq errors))]
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
            (when done-action (control/render-control master-form done-action))
            (ui-box {:className (when done-action "grow flex-1 flex-row items-center justify-center")}
              (if (string? title)
                (ui-heading {:size "md" :className "text-typography-900 font-semibold"} title)
                title))
            (when (seq rest-actions)
              (ui-button-group {:space "sm" :flexDirection "row"}
                (keep #(control/render-control master-form %) rest-actions)))))))))

(defsc StandardFormContainer [this {::form/keys [props computed-props form-instance master-form] :as env}]
  {:shouldComponentUpdate (fn [_ _ _] true)})

(def standard-form-container (comp/factory StandardFormContainer))

