(ns com.fulcrologic.rad.rendering.gluestack-ui.controls.instant-inputs
  "Date and time input controls for Fulcro RAD using GlueStack UI.
   Provides various datetime input formats optimized for mobile/React Native."
  (:require
    [clojure.string :as str]
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    [com.fulcrologic.rad.type-support.date-time :as dt]
    [com.fulcrologic.rad.rendering.gluestack-ui.controls.control :as control]
    [com.fulcrologic.gluestack-ui.components.ui.box :refer [ui-box]]
    [com.fulcrologic.gluestack-ui.components.ui.input :refer [ui-input ui-input-field]]
    [com.fulcrologic.gluestack-ui.components.ui.pressable :refer [ui-pressable]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [cljc.java-time.local-time :as lt]
    [cljc.java-time.local-date-time :as ldt]))
    ;#?(:cljs ["react-native-date-picker" :default DatePicker])))

;; React Native component wrapper for DatePicker
;#?(:cljs
;   (def ui-date-picker (react-factory DatePicker)))

(defsc DateTimeInput [this {:keys [value onChangeText isDisabled mode ::default-local-time] :or {mode "datetime"} :as props}]
  {:initLocalState (fn [_] {:showing-picker? false})
   :shouldComponentUpdate (fn [_ _ _] true)}
  #_(let [showing-picker? (comp/get-state this :showing-picker?)
        formatted-value (cond
                          (nil? value) ""
                          (= mode "time")
                          (let [full-datetime (dt/inst->html-datetime-string value)]
                            (if (re-find #"T" full-datetime)
                              (second (str/split full-datetime #"T"))
                              ""))
                          (= mode "date") (dt/inst->html-date value)
                          :else (dt/inst->html-datetime-string value))
        toggle-picker #(comp/set-state! this {:showing-picker? (not showing-picker?)})
        local-time (or default-local-time lt/noon)

        ;; Convert to JS Date for react-native-date-picker
        js-date-value #?(:cljs (if value (js/Date. (inst-ms value)) (js/Date.))
                         :clj nil)

        on-date-selected (fn [selected-date]
                           #?(:cljs
                              (when (and selected-date onChangeText)
                                (case mode
                                  "time"
                                  (let [today (js/Date.)
                                        new-date (js/Date.)]
                                    (.setFullYear new-date (.getFullYear today))
                                    (.setMonth new-date (.getMonth today))
                                    (.setDate new-date (.getDate today))
                                    (.setHours new-date (.getHours selected-date))
                                    (.setMinutes new-date (.getMinutes selected-date))
                                    (.setSeconds new-date 0)
                                    (onChangeText new-date)
                                    (comp/set-state! this {:showing-picker? false}))

                                  "date"
                                  (let [d (js/Date. (.getTime selected-date))]
                                    (if (= local-time lt/noon)
                                      ;; Set to noon
                                      (do (.setHours d 12)
                                          (.setMinutes d 0)
                                          (.setSeconds d 0))
                                      ;; Set to midnight
                                      (do (.setHours d 0)
                                          (.setMinutes d 0)
                                          (.setSeconds d 0)))
                                    (onChangeText d)
                                    (comp/set-state! this {:showing-picker? false}))

                                  ;; default: datetime
                                  (do
                                    (onChangeText selected-date)
                                    (comp/set-state! this {:showing-picker? false}))))))]
    (ui-box {:width "100%"}
      (ui-pressable {:onPress toggle-picker
                     :disabled isDisabled}
        (ui-input {:isDisabled isDisabled
                  :isReadOnly true}
          (ui-input-field {:value formatted-value
                          :placeholder (case mode
                                         "time" "Select time..."
                                         "date" "Select date..."
                                         "Select date and time...")})))

      #?(:cljs
         (when showing-picker?
           (ui-box {:mt "2" :borderWidth "1" :borderColor "gray.200" :borderRadius "md" :p "2"}
             (ui-date-picker {:mode mode
                              :date js-date-value
                              :onDateChange on-date-selected
                              :textColor "#2A9D8F"})))))))

(def ui-date-time-input (comp/factory DateTimeInput))

;; Standard date-time input (full datetime picker)
(defn ui-date-time-instant-input [_render-env props]
  (ui-date-time-input (assoc props :mode "datetime")))

;; Date picker that sets time to given local-time (default noon)
(defn ui-date-instant-input [{::keys [default-local-time]} props]
  (ui-date-time-input (assoc props
                             :mode "date"
                             ::default-local-time (or default-local-time lt/noon))))

;; Date picker that sets the date as the day before, and time as midnight
;; Used for inclusive/exclusive date range end points
(defn ui-ending-date-instant-input [_ props]
  #_(let [value (:value props)
        adjusted-value (when value
                         (-> value
                             dt/inst->local-datetime
                             (ldt/minus-days 1)
                             dt/local-datetime->inst))

        on-change (:onChangeText props)
        adjusted-on-change (fn [new-value]
                             (when (and new-value on-change)
                               ;; Add a day to the selected date
                               #?(:cljs
                                  (let [d (js/Date. (inst-ms new-value))]
                                    (.setDate d (+ (.getDate d) 1))
                                    (on-change d))
                                  :clj
                                  (let [adjusted-new-value (-> new-value
                                                             dt/inst->local-datetime
                                                             (ldt/plus-days 1)
                                                             dt/local-datetime->inst)]
                                    (on-change adjusted-new-value)))))]
    (ui-date-time-input (-> props
                            (assoc :mode "date"
                                   :value adjusted-value
                                   :onChangeText adjusted-on-change)))))

;; Time-only picker (unique to GlueStack UI implementation)
(defn ui-time-instant-input [_ props]
  (ui-date-time-input (assoc props :mode "time")))

;; Control renderers that work with RAD control framework

(defn date-time-control [render-env]
  (control/ui-control (assoc render-env :input-factory ui-date-time-instant-input)))

(defn midnight-on-date-control [render-env]
  (control/ui-control (assoc render-env
                       :input-factory ui-date-instant-input
                       ::default-local-time lt/midnight)))

(defn midnight-next-date-control [render-env]
  (control/ui-control (assoc render-env
                       :input-factory ui-ending-date-instant-input)))

(defn date-at-noon-control [render-env]
  (control/ui-control (assoc render-env
                       ::default-local-time lt/noon
                       :input-factory ui-date-instant-input)))

(defn time-only-control [render-env]
  (control/ui-control (assoc render-env
                       :input-factory ui-time-instant-input)))
