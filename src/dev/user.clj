(ns user
  (:require
    [clojure.string :as str]
    [clojure.java.io :as io :refer [as-file file make-parents reader]]
    [snitch.core :refer [defn* defmethod* *fn *let]]))

(defn hyphenated [camelCase]
  (-> camelCase
    (str/replace #"([A-Z])" "-$1")
    (str/lower-case)
    (str/replace #"^-" "")))

(defn get-relative-path [parent-file child-file]
  (let [parent-path (.toPath parent-file)
        child-path  (.toPath child-file)]
    (str (.toString (.relativize parent-path child-path)))))

;(defn extract-exported-names [file-path]
;  (let [content (->> (slurp file-path)
;                  (str/split-lines)                        ; Split input into lines
;                  (filter #(re-matches #"^\s*export\s+.*" %))
;                  (str/join " "))                          ;; Keep only lines starting with 'export'
;        matches (concat
;                  ;; Match named exports: `export { Something as Name }`
;                  (map second (re-seq #"as\s+(\w+)" content))
;                  ;; Match `export const|let|var VariableName =`
;                  (map second (re-seq #"export\s+(?:const|let|var)\s+(\w+)" content)))]
;    (->> matches
;      (distinct)
;      (vec))))

;(defn extract-class-refs [file-path]
;  (*let [content (slurp file-path)
;         exports (when-let [exports (re-seq #"export\s*\{([^}]+)\}" content)]
;                   exports
;                   #_(->> (str/split (str/join " " (mapv second exports)) #",")
;                       (map str/trim)
;                       (filter not-empty)))
;         functions (map second (re-seq #"export function (\w+)\(" content))]
;    (->> (concat exports functions)
;      (distinct)
;      (vec))))

(defn extract-class-refs [file-path]
  (let [content (slurp file-path)
        exports (when-let [exports (mapv second (re-seq #"export\s*\{([^}]+)\}" content))]
                  (mapcat (fn [export]
                            (let [matches (re-seq #"(?:\w+\s+as\s+(\w+))|(\w+)" export)]
                              (->> matches
                                (mapv (fn [[_ m1 m2]] (flatten [m1 m2])))
                                flatten
                                (filter not-empty))))
                    exports))
         functions (map second (re-seq #"export function (\w+)\(" content))]
    (->> (concat exports functions)
      (distinct)
      (vec))))

;(defn extract-exported-names [file-path]
;  (when-let [[_ exports] (re-find #"export\s*\{([^}]+)\}" (slurp file-path))]
;    (->> (str/split exports #",")
;      (map str/trim)
;      (filter not-empty)
;      (distinct)
;      (vec))))

(defn factory-preamble [ns-name module-dep factory-refer]
  (str "(ns " ns-name "\n"
    "  (:require\n"
    (when factory-refer
      (str "    [com.fulcrologic.fulcro-native.alpha.components :refer [" (str/join " " factory-refer) "]]\n"))
    "    " module-dep "))\n\n"))

(defn matches-pattern? [patterns s]
  (boolean
    (some (fn [pattern]
            (if (.contains pattern "*")
              (re-matches (re-pattern (str (str/replace pattern "*" "[A-Za-z]+") "$")) s)
              (= pattern s)))
      patterns)))

(defn input-factory-classes? [class-ref]
  (let [patterns #{"InputField" "Checkbox" "Textarea"}]
    (matches-pattern? patterns class-ref)))

(defn fn-factory-class? [class-ref]
  (let [patterns #{"*Icon"}]
    (matches-pattern? patterns class-ref)))


(defn factory-helper [class-ref]
  (cond
    (input-factory-classes? class-ref) "wrap-text-input"
    (fn-factory-class? class-ref) nil
    :else "react-factory"))

(defn factory-options-helper [class-ref]
  (cond
    (= class-ref "AvatarFallbackText") "{:ui-text false}"
    :else nil))

(defn factory-def-helper [{:keys [factory-name factory-fn factory-fn-options class-ref docstring]}]
  (let [docstring-line (when docstring (str "  \"" docstring "\"\n"))
        factory-parts (filterv not-empty [factory-fn class-ref factory-fn-options])
        factory (->> [factory-fn class-ref factory-fn-options]
                  (filter not-empty)
                  (str/join " "))]
    (str "(def " factory-name "\n"
      docstring-line
      (if (= 1 (count factory-parts))
        (str "  #?(:cljs " (str/join " " factory-parts) "))\n")
        (str "  #?(:cljs (" (str/join " " factory-parts) ")))\n")))))

(defn generate-factory-map [class-ref]
  (hash-map
    :class-ref class-ref
    :factory-fn (factory-helper class-ref)
    :factory-fn-options (factory-options-helper class-ref)
    :factory-name (str "ui-" (hyphenated class-ref))
    :docstring (str class-ref "Factory")))

(defn generate-module-map [module-base-path ui-path ui-file]
  (let [js-path              (.getPath ui-file)
        relative-js-path     (when-let [[_ path] (re-find (re-pattern (str ".*" ui-path "(.*)")) js-path)]
                               (-> (str ui-path path)
                                 (str/replace #"\.tsx$" ".js")))
        relative-module-path (-> (str/replace relative-js-path #"\/index\.js$" ".cljc")
                               (str/replace #"-" "_"))
        module-path          (str module-base-path relative-module-path)
        module-name          (->> (str/split (str/replace relative-module-path #"\.cljc" "") #"/")
                               (filter not-empty)
                               (last))
        factory-name         (str "ui-" (hyphenated module-name))
        ns-name              (-> (str/replace module-path #"^src/main/" "")
                               (str/replace #"\.cljc$" "")
                               (str/replace #"/" ".")
                               (str/replace #"\_" "-"))
        class-refs           (extract-class-refs js-path)
        module-dep           (str
                               "#?(:cljs ["
                               (str "\"@" relative-js-path "\"")
                               " :refer [" (str/join " " class-refs)
                               "]])")
        factories            (mapv generate-factory-map class-refs)]
    {:js-path              js-path
     :relative-js-path     relative-js-path
     :relative-module-path relative-module-path
     :module-path          module-path
     :module-name          module-name
     :factory-name         factory-name
     :ns-name              ns-name
     :module-dep           module-dep
     :class-refs           class-refs
     :factories            factories}))

(defn module->cljc-file [{:keys [ns-name module-dep factories] :as module}]
  (let [preamble (factory-preamble ns-name module-dep (distinct (mapv :factory-fn factories)))
        defs     (map factory-def-helper (:factories module))]
    (str preamble (str/join "\n" defs))))

(defn filter-gluestack-files [files]
  (->> files
    (filter #(and
               (.isFile %)
               (re-matches #".*/components/ui/[^/]+/index\.tsx$" (.getPath %))))))

(def icon-factory-preamble
  (str "(ns com.fulcrologic.gluestack-ui.components.lucide-icons\n"
    "  (:require\n"
    "    #?(:cljs [\"lucide-react-native\" :as lucide])\n"
    "    [com.fulcrologic.fulcro.components :as comp]))\n\n"
    "(def icon #?(:cljs lucide :clj {}))\n\n"))

(defn icon-factory-def-helper [class-ref factory-name]
  (str "(def " factory-name " (comp/isoget icon \"" class-ref "\"))"))

(defn icons->cljc-file [modules]
  (*let [defs (mapv (fn [[class-ref factory-name]]
                      (icon-factory-def-helper class-ref factory-name))
                modules)]
    (str icon-factory-preamble (str/join "\n" defs))))

(defn generate-gluestack-ui [parent-path ui-paths module-base-path]
  (let [full-dirs (map #(vector % (str parent-path %)) ui-paths)
        js-files  (->> full-dirs
                    (mapv (fn [[ui-path full-ui-path]]
                            (vector
                              ui-path
                              (->> full-ui-path
                                (file)
                                (file-seq)
                                (filter-gluestack-files)
                                (sort-by #(.getPath %))
                                (vec))))))
        modules   (->> js-files
                    (mapcat (fn [[ui-path ui-files]]
                              (map #(generate-module-map module-base-path ui-path %) ui-files))))]
    (doseq [{:keys [module-path] :as module} modules]
      (make-parents module-path)
      (spit (as-file module-path) (module->cljc-file module)))))

(defn generate-lucide-react-native-icons [module-base-path entry-path]
  (let [filename (str module-base-path "/lucide_icons.cljc")
        modules (->> (slurp (file entry-path))
                  (re-seq #"exports\.([A-Z][a-zA-Z0-9]*)Icon")
                  (map last)
                  (distinct)
                  (filter some?)
                  (sort)
                  (mapv #(vector % (str (hyphenated %) "-icon"))))]
    (make-parents filename)
    (spit (as-file filename) (icons->cljc-file modules))))

(comment
  (def parent-path "/Users/yawodame/development/fulcrologic/fulcro-gluestack-ui/app")
  (def ui-paths ["/components/ui"])
  (def module-base-path "src/main/com/fulcrologic/gluestack_ui")

  (generate-gluestack-ui parent-path ui-paths module-base-path)

  (def icon-base-path "src/main/com/fulcrologic/gluestack_ui/components")
  (def icon-entry-path "/Users/yawodame/development/fulcrologic/fulcro-gluestack-ui/app/node_modules/lucide-react-native/dist/cjs/lucide-react-native.js")
  (generate-lucide-react-native-icons icon-base-path icon-entry-path)

  modules

  nil)
