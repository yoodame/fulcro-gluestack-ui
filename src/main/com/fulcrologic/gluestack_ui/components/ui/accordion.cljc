(ns com.fulcrologic.gluestack-ui.components.ui.accordion
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory]]
    #?(:cljs ["@/components/ui/accordion/index.js" :refer [Accordion AccordionItem AccordionHeader AccordionTrigger AccordionTitleText AccordionContentText AccordionIcon AccordionContent]])))

(def ui-accordion
  "Accordion Factory"
  #?(:cljs (react-factory Accordion)))

(def ui-accordion-item
  "AccordionItem Factory"
  #?(:cljs (react-factory AccordionItem)))

(def ui-accordion-header
  "AccordionHeader Factory"
  #?(:cljs (react-factory AccordionHeader)))

(def ui-accordion-trigger
  "AccordionTrigger Factory"
  #?(:cljs (react-factory AccordionTrigger)))

(def ui-accordion-title-text
  "AccordionTitleText Factory"
  #?(:cljs (react-factory AccordionTitleText)))

(def ui-accordion-content-text
  "AccordionContentText Factory"
  #?(:cljs (react-factory AccordionContentText)))

(def ui-accordion-icon
  "AccordionIcon Factory"
  #?(:cljs (react-factory AccordionIcon)))

(def ui-accordion-content
  "AccordionContent Factory"
  #?(:cljs (react-factory AccordionContent)))
