(ns com.fulcrologic.rad.rendering.gluestack-ui.instant-field
  "GlueStack UI renderer for instant (date/time) fields in Fulcro RAD forms."
  (:require
    [clojure.string :as str]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    [com.fulcrologic.rad.type-support.date-time :as datetime]
    [com.fulcrologic.rad.rendering.gluestack-ui.field :refer [render-field-factory]]
    [com.fulcrologic.gluestack-ui.components.ui.input :refer [ui-input ui-input-field]]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]))
    ;#?(:cljs ["react-native-date-picker" :default DatePicker])))

;#?(:cljs
;   (def ui-date-picker (react-factory DatePicker)))

(defsc DateTimeInputComponent [this {:keys [value onChangeText isReadOnly placeholder] :as props}]
  {:initLocalState (fn [_] {:showing-picker? false})
   :shouldComponentUpdate (fn [_ _ _] true)}
  #_(let [showing-picker? (comp/get-state this :showing-picker?)
        formatted-value (if value
                          (datetime/inst->html-datetime-string value)
                          "")
        toggle-picker #(comp/set-state! this {:showing-picker? (not showing-picker?)})

        ;; Convert string to JS Date for react-native-date-picker
        js-date-value #?(:cljs (if value (js/Date. (inst-ms value)) (js/Date.))
                        :clj nil)

        on-date-selected (fn [selected-date]
                           #?(:cljs
                              (when (and selected-date onChangeText)
                                ;; Convert JS Date to inst
                                (let [inst (js/Date. (.getTime selected-date))]
                                  (onChangeText inst)
                                  (comp/set-state! this {:showing-picker? false})))))]
    (ui-box {:width "100%"}
      (ui-input {:isReadOnly isReadOnly}
        (ui-input-field {:value formatted-value
                         :placeholder (or placeholder "Select date and time...")
                         :onPressIn (when-not isReadOnly toggle-picker)}))

      #?(:cljs
         (when showing-picker?
           (ui-box {:mt "2" :borderWidth "1" :borderColor "gray.200" :borderRadius "md" :p "2"}
             (ui-date-picker {:mode "datetime"
                              :date js-date-value
                              :onDateChange on-date-selected
                              :textColor "#2A9D8F"})))))))

(def ui-datetime-input (comp/factory DateTimeInputComponent))

(defsc DateNoonInputComponent [this {:keys [value onChangeText isReadOnly placeholder] :as props}]
  {:initLocalState (fn [_] {:showing-picker? false})
   :shouldComponentUpdate (fn [_ _ _] true)}
  #_(let [showing-picker? (comp/get-state this :showing-picker?)
        ;; Only show date part (remove time)
        formatted-value (if value
                          (str/replace (datetime/inst->html-datetime-string value) #"T.*$" "")
                          "")
        toggle-picker #(comp/set-state! this {:showing-picker? (not showing-picker?)})

        ;; Convert to JS Date
        js-date-value #?(:cljs (if value (js/Date. (inst-ms value)) (js/Date.))
                        :clj nil)

        on-date-selected (fn [selected-date]
                           #?(:cljs
                              (when (and selected-date onChangeText)
                                ;; Set time to noon
                                (let [d (js/Date. (.getTime selected-date))]
                                  (.setHours d 12)
                                  (.setMinutes d 0)
                                  (.setSeconds d 0)
                                  (onChangeText d)
                                  (comp/set-state! this {:showing-picker? false})))))]
    (ui-box {:width "100%"}
      (ui-input {:isReadOnly isReadOnly}
        (ui-input-field {:value formatted-value
                         :placeholder (or placeholder "Select date...")
                         :onPressIn (when-not isReadOnly toggle-picker)}))

      #?(:cljs
         (when showing-picker?
           (ui-box {:mt "2" :borderWidth "1" :borderColor "gray.200" :borderRadius "md" :p "2"}
             (ui-date-picker {:mode "date"
                              :date js-date-value
                              :onDateChange on-date-selected
                              :textColor "#2A9D8F"})))))))

(defsc MidnightDateInputComponent [this {:keys [value onChangeText isReadOnly placeholder] :as props}]
  {:initLocalState (fn [_] {:showing-picker? false})
   :shouldComponentUpdate (fn [_ _ _] true)}
  #_(let [showing-picker? (comp/get-state this :showing-picker?)
        ;; Only show date part (remove time)
        formatted-value (if value
                          (str/replace (datetime/inst->html-datetime-string value) #"T.*$" "")
                          "")
        toggle-picker #(comp/set-state! this {:showing-picker? (not showing-picker?)})

        ;; Convert to JS Date
        js-date-value #?(:cljs (if value (js/Date. (inst-ms value)) (js/Date.))
                        :clj nil)

        on-date-selected (fn [selected-date]
                           #?(:cljs
                              (when (and selected-date onChangeText)
                                ;; Set time to midnight
                                (let [d (js/Date. (.getTime selected-date))]
                                  (.setHours d 0)
                                  (.setMinutes d 0)
                                  (.setSeconds d 0)
                                  (onChangeText d)
                                  (comp/set-state! this {:showing-picker? false})))))]
    (ui-box {:width "100%"}
      (ui-input {:isReadOnly isReadOnly}
        (ui-input-field {:value formatted-value
                         :placeholder (or placeholder "Select date (midnight)...")
                         :onPressIn (when-not isReadOnly toggle-picker)}))

      #?(:cljs
         (when showing-picker?
           (ui-box {:mt "2" :borderWidth "1" :borderColor "gray.200" :borderRadius "md" :p "2"}
             (ui-date-picker {:mode "date"
                              :date js-date-value
                              :onDateChange on-date-selected
                              :textColor "#2A9D8F"})))))))

(defsc MidnightNextDateInputComponent [this {:keys [value onChangeText isReadOnly placeholder] :as props}]
  {:initLocalState (fn [_] {:showing-picker? false})
   :shouldComponentUpdate (fn [_ _ _] true)}
  #_(let [showing-picker? (comp/get-state this :showing-picker?)
        ;; Adjust date for display (one day before)
        adjusted-value (when value
                         (-> value
                             datetime/inst->local-datetime
                             (#?(:cljs #(.minus ^js % #js {:days 1})
                                 :clj #(.minusDays % 1)))
                             datetime/local-datetime->inst))
        ;; Only show date part (remove time)
        formatted-value (if adjusted-value
                          (str/replace (datetime/inst->html-datetime-string adjusted-value) #"T.*$" "")
                          "")
        toggle-picker #(comp/set-state! this {:showing-picker? (not showing-picker?)})

        ;; Convert adjusted date to JS Date
        js-date-value #?(:cljs (if adjusted-value (js/Date. (inst-ms adjusted-value)) (js/Date.))
                        :clj nil)

        on-date-selected (fn [selected-date]
                           #?(:cljs
                              (when (and selected-date onChangeText)
                                ;; Set to midnight and add a day
                                (let [d (js/Date. (.getTime selected-date))]
                                  (.setHours d 0)
                                  (.setMinutes d 0)
                                  (.setSeconds d 0)
                                  (.setDate d (+ (.getDate d) 1))
                                  (onChangeText d)
                                  (comp/set-state! this {:showing-picker? false})))))]
    (ui-box {:width "100%"}
      (ui-input {:isReadOnly isReadOnly}
        (ui-input-field {:value formatted-value
                         :placeholder (or placeholder "Select end date...")
                         :onPressIn (when-not isReadOnly toggle-picker)}))

      #?(:cljs
         (when showing-picker?
           (ui-box {:mt "2" :borderWidth "1" :borderColor "gray.200" :borderRadius "md" :p "2"}
             (ui-date-picker {:mode "date"
                              :date js-date-value
                              :onDateChange on-date-selected
                              :textColor "#2A9D8F"})))))))

(defsc TimeOnlyInputComponent [this {:keys [value onChangeText isReadOnly placeholder] :as props}]
  {:initLocalState (fn [_] {:showing-picker? false})
   :shouldComponentUpdate (fn [_ _ _] true)}
  #_(let [showing-picker? (comp/get-state this :showing-picker?)
        ;; Extract and format time part from datetime
        formatted-value (if value
                          ;; Extract just the time portion HH:MM from ISO format
                          (let [full-datetime (datetime/inst->html-datetime-string value)]
                            (if (re-find #"T" full-datetime)
                              (second (str/split full-datetime #"T"))
                              ""))
                          "")
        toggle-picker #(comp/set-state! this {:showing-picker? (not showing-picker?)})

        ;; Convert to JS Date
        js-date-value #?(:cljs (if value (js/Date. (inst-ms value)) (js/Date.))
                        :clj nil)

        on-time-selected (fn [selected-date]
                           #?(:cljs
                              (when (and selected-date onChangeText)
                                ;; Keep just the time part, combine with today's date
                                (let [today (js/Date.)
                                      new-date (js/Date.)]
                                  (.setFullYear new-date (.getFullYear today))
                                  (.setMonth new-date (.getMonth today))
                                  (.setDate new-date (.getDate today))
                                  (.setHours new-date (.getHours selected-date))
                                  (.setMinutes new-date (.getMinutes selected-date))
                                  (.setSeconds new-date (.getSeconds selected-date))
                                  (onChangeText new-date)
                                  (comp/set-state! this {:showing-picker? false})))))]
    (ui-box {:width "100%"}
      (ui-input {:isReadOnly isReadOnly}
        (ui-input-field {:value formatted-value
                         :placeholder (or placeholder "Select time...")
                         :onPressIn (when-not isReadOnly toggle-picker)}))

      #?(:cljs
         (when showing-picker?
           (ui-box {:mt "2" :borderWidth "1" :borderColor "gray.200" :borderRadius "md" :p "2"}
             (ui-date-picker {:mode "time"
                              :date js-date-value
                              :onDateChange on-time-selected
                              :textColor "#2A9D8F"})))))))

(def ui-date-noon-input (comp/factory DateNoonInputComponent))
(def ui-midnight-date-input (comp/factory MidnightDateInputComponent))
(def ui-midnight-next-date-input (comp/factory MidnightNextDateInputComponent))
(def ui-time-only-input (comp/factory TimeOnlyInputComponent))

(def render-field
  "Renders a datetime field that collects both date and time values."
  (render-field-factory ui-datetime-input))

(def render-date-at-noon-field
  "Renders a date-only field that sets the time to noon."
  (render-field-factory ui-date-noon-input))

(def render-midnight-on-date-field
  "Renders a date-only field that sets the time to midnight (00:00)."
  (render-field-factory ui-midnight-date-input))

(def render-midnight-next-date-field
  "Renders a date-only field that sets the time to midnight of the next day.
   Useful for inclusive/exclusive date range endpoints."
  (render-field-factory ui-midnight-next-date-input))

(def render-time-only-field
  "Renders a time-only field without the date component."
  (render-field-factory ui-time-only-input))
