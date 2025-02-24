(ns com.fulcrologic.rad.rendering.gluestack-ui-options
  "Documented option keys for setting rendering-specific customization
  options when using Gluestack UI Plugin as your UI renderer.

  ALL options MUST appear under the rendering options key:

  ```
  (ns ...
    (:require
       [com.fulcrologic.rad.gluestack-ui-options :as guo]
       ...))

  (defsc-report Report [this props]
    {guo/rendering-options { ... }}}
  ```

  Most of the options in this file can be given a global default using

  ```
  (set-global-rendering-options! fulcro-app options)
  ```

  where the `options` is a map of option keys/values.
  "
  (:require
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.fulcro.components :as comp]))

(def rendering-options
  "Top-level key for specifying rendering options. All
   GUI customization options MUST appear under this key."
  ::rendering-options)

(def layout-class
  "The CSS class of the div that holds the top-level layout of the report or form.

   A string or `(fn [instance] string?)`."
  ::layout-class)

(defn get-rendering-options
  "Get rendering options from a mounted component `c`.

   WARNING: If c is a class, then global overrides will not be honored."
  ([c & ks]
   (let [app            (comp/any->app c)
         global-options (some-> app
                          :com.fulcrologic.fulcro.application/runtime-atom
                          deref
                          ::rendering-options)
         options        (merge
                          global-options
                          (comp/component-options c ::rendering-options))]
     (if (seq ks)
       (get-in options (vec ks))
       options))))

(defn set-global-rendering-options!
  "Set rendering options on the application such that they serve as *defaults*.

  The `options` parameter to this function MUST NOT have the key guo/rendering-options, but
  should instead just have the parameters themselves (e.g. ::guo/action-button-renderer).
  "
  [app options]
  (swap! (:com.fulcrologic.fulcro.application/runtime-atom app)
    assoc
    ::rendering-options
    options))
