(ns com.fulcrologic.gluestack-ui.components.ui.accordion
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory ]]
    #?(:cljs ["@/components/ui/accordion/index.js" :refer [Accordion AccordionItem AccordionHeader AccordionTrigger AccordionTitleText AccordionContentText AccordionIcon AccordionContent]])))

(def ui-accordion
  "AccordionFactory"
  #?(:cljs (react-factory Accordion)))

(def ui-accordion-item
  "AccordionItemFactory"
  #?(:cljs (react-factory AccordionItem)))

(def ui-accordion-header
  "AccordionHeaderFactory"
  #?(:cljs (react-factory AccordionHeader)))

(def ui-accordion-trigger
  "AccordionTriggerFactory"
  #?(:cljs (react-factory AccordionTrigger)))

(def ui-accordion-title-text
  "AccordionTitleTextFactory"
  #?(:cljs (react-factory AccordionTitleText)))

(def ui-accordion-content-text
  "AccordionContentTextFactory"
  #?(:cljs (react-factory AccordionContentText)))

(def ui-accordion-icon
  "AccordionIconFactory"
  #?(:cljs AccordionIcon))

(def ui-accordion-content
  "AccordionContentFactory"
  #?(:cljs (react-factory AccordionContent)))
