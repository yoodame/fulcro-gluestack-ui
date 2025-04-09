(ns com.fulcrologic.rad.rendering.gluestack-ui.utils
  (:require
    #?(:cljs [cognitect.transit :as transit])
    [taoensso.timbre :as log]))

;; Transit encoding/decoding functions
(defn transit->str
  "Encode a Clojure data structure into a JSON string."
  [x]
  #?(:clj  x
     :cljs (when x
             (let [w (transit/writer :json)]
               (transit/write w x)))))

(defn str->transit
  "Decode a JSON string into a Clojure data structure."
  [s]
  #?(:clj  s
     :cljs (when s
             (try
               (let [r (transit/reader :json)]
                 (transit/read r s))
               (catch :default e
                 (log/error "Error reading Transit:" s e)
                 nil)))))
