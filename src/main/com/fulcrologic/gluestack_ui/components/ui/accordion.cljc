(ns com.fulcrologic.gluestack-ui.components.ui.accordion
  (:require
    [com.fulcrologic.fulcro-native.alpha.components :refer [react-factory ]]
    #?(:cljs ["@/components/ui/accordion/index.js" :refer [Accordion AccordionItem AccordionHeader AccordionTrigger AccordionTitleText AccordionContentText AccordionIcon AccordionContent]])))

(def ui-accordion
  "React native factory for Accordion"
  #?(:cljs (react-factory Accordion)))

(def ui-accordion-item
  "React native factory for AccordionItem"
  #?(:cljs (react-factory AccordionItem)))

(def ui-accordion-header
  "React native factory for AccordionHeader"
  #?(:cljs (react-factory AccordionHeader)))

(def ui-accordion-trigger
  "React native factory for AccordionTrigger"
  #?(:cljs (react-factory AccordionTrigger)))

(def ui-accordion-title-text
  "React native factory for AccordionTitleText"
  #?(:cljs (react-factory AccordionTitleText)))

(def ui-accordion-content-text
  "React native factory for AccordionContentText"
  #?(:cljs (react-factory AccordionContentText)))

(def ui-accordion-icon
  "React native factory for AccordionIcon"
  #?(:cljs AccordionIcon))

(def ui-accordion-content
  "React native factory for AccordionContent"
  #?(:cljs (react-factory AccordionContent)))
