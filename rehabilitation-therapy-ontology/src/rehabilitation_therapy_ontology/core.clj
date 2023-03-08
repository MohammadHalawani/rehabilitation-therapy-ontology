(ns rehabilitation-therapy-ontology.core
  (:use [tawny owl pattern util])
  (:require [tawny.lookup :as l]
            [tawny.reasoner :as r]
            [tawny.query :as q]
            [clojure.string :as str]))

(defontology rehabilitation-therapy-ontology
  :iri "http://www.ncl.ac.uk/rto")


;; The main classes (exampelars)
(declare-classes Rehablitation Therapist Gait Aphasia)

;; Taxonomies of subclasses for the main classes
(deftier Rehablitation
  [
   Occupational
   Physio
   SpeechLanguage
   ]
  :suffix :Therapy
  :property false
  :cover false
  :comment "Top ranked subclasses from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv")
  )

(defclass DailyOccupationPerformanceTraining
  :super OccupationalTherapy
  :comment "Derived from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv"))
(defclass ConventionalPhysioTherapy
  :super PhysioTherapy
  :comment "Derived from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv"))

(defclass ConstraintInducedLanguageTherapy
  :super SpeechLanguageTherapy
  :comment "Constraint-induced aphasia therapy (CIAT) is an intensive therapy model based on the forced use of verbal oral language as the sole channel of communication, while any alternative communication mode such as writing, gesturing or pointing are prevented."
  :annotation (see-also "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5619412/")
  :comment "Derived from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv"))


(deftier DailyOccupationPerformanceTraining
  [
   CognitiveOrientation
   KnowledgeTranslation
   ]
  :prefix :DailyOccupationPerformance
  :property false
  :cover false
  :comment "Top ranked subclasses from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv")
  )

(deftier Aphasia
  [
   ChronicPostStroke
   Anomic
   Conduction
   ]
  :suffix :Aphasia
  :property false
  :cover false
  :comment "Top ranked subclasses from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv")
  )

(defclass ProgressiveAphasia
  :super ConductionAphasia
  :comment "Derived from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv"))
(defclass PrimaryProgressiveAphasia
  :super ProgressiveAphasia
  :comment "Derived from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv"))

(deftier Therapist
  [
   Occupational
   Physio
   ]
  :suffix :Therapist
  :property false
  :cover false
  :comment "Top ranked subclasses from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv")
  )

(defclass NursePhysioTherapist
  :super PhysioTherapist
  :comment "Derived from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv"))

;; Upper classes and object properties that can be used to classify therapies follow the sentence:
;; Therapist performs Therapy treats Disorder targets (Body Part or Function)
(defclass Disorder
  :comment "Any abnormal condition of the body or mind that causes discomfort, dysfunction, or distress to the person affected or those in contact with the person. The term is often used broadly to include injuries, disabilities, syndromes, symptoms, deviant behaviors, and atypical variations of structure and function."
  :comment "http://purl.obolibrary.org/obo/NCIT_C2991")
(defclass BodyPart
  :comment "Part of the Body Structures"
  :comment "http://who.int/icf#s")
(defclass Function
  :comment "The normal action performed by the Body Functions"
  :comment "http://who.int/icf#b")

;; equivalent classes
(defclass Therapy
  :equivalent Rehablitation)
(defclass PhysicalTherapy
  :equivalent PhysioTherapy)
(defclass PhysicalTherapist
  :equivalent PhysioTherapist)

;; object properties
(defoproperty perform
  :domain Therapist
  :range Therapy)
(defoproperty treat
  :domain Therapy
  :range Disorder)
(defoproperty target
  :domain Therapy
  :range BodyPart Function)

;; Refine the main classes to classify them under the upper ontology classes
(refine Aphasia
        :super Disorder)
(refine Gait
        :super Function)

;; An object property (targets) that replaces the subclass relationship between Gait and Treadmill Training in the semantic graph
(defclass TreadmillTraining
  :super Therapy (owl-some target Gait)
  :comment "modified from the semantic graph"
  :annotation (see-also "https://github.com/MohammadHalawani/PhD-datasets/terms-subclasses.csv"))

;; Refining classes
;; definitions and mapping to MeSH terms
(refine Rehablitation
        :comment "Restoration of human functions to the maximum degree possible in a person or persons suffering from disease or injury."
        :comment "http://purl.bioontology.org/ontology/MESH/D012046")
(refine Therapy
        :comment "Used with diseases for therapeutic interventions except drug therapy, diet therapy, radiotherapy, and surgery, for which specific subheadings exist. The concept is also used for articles and books dealing with multiple therapies."
        :comment "http://purl.bioontology.org/ontology/MESH/Q000628")
(refine Therapist
        :comment "Person trained in Rehabilitation to make use of Therapies to prevent, correct, and alleviate disorders."
        :comment "Derived from http://purl.bioontology.org/ontology/MESH/D059825")
(refine Gait
        :comment "Manner or style of walking."
        :comment "http://purl.bioontology.org/ontology/MESH/D005684")
(refine Aphasia
        :comment "A cognitive disorder marked by an impaired ability to comprehend or express language in its written or spoken form. This condition is caused by diseases which affect the language areas of the dominant hemisphere. Clinical features are used to classify the various subtypes of this condition. General categories include receptive, expressive, and mixed forms of aphasia."
        :comment "http://purl.bioontology.org/ontology/MESH/D001037")
(refine AnomicAphasia
        :comment "A language dysfunction characterized by the inability to name people and objects that are correctly perceived. The individual is able to describe the object in question, but cannot provide the name. This condition is associated with lesions of the dominant hemisphere involving the language areas, in particular the TEMPORAL LOBE. (From Adams et al., Principles of Neurology, 6th ed, p484)"
        :comment "http://purl.bioontology.org/ontology/MESH/D000849")
(refine ConductionAphasia
        :comment "A type of fluent aphasia characterized by an impaired ability to repeat one and two word phrases, despite retained comprehension. This condition is associated with dominant hemisphere lesions involving the arcuate fasciculus (a white matter projection between Broca's and Wernicke's areas) and adjacent structures. Like patients with Wernicke aphasia (APHASIA, WERNICKE), patients with conduction aphasia are fluent but commit paraphasic errors during attempts at written and oral forms of communication. (From Adams et al., Principles of Neurology, 6th ed, p482; Brain & Bannister, Clinical Neurology, 7th ed, p142; Kandel et al., Principles of Neural Science, 3d ed, p848)"
        :comment "http://purl.bioontology.org/ontology/MESH/D018886")
(refine PrimaryProgressiveAphasia
        :comment "A progressive form of dementia characterized by the global loss of language abilities and initial preservation of other cognitive functions. Fluent and nonfluent subtypes have been described. Eventually a pattern of global cognitive dysfunction, similar to ALZHEIMER DISEASE, emerges. Pathologically, there are no Alzheimer or PICK DISEASE like changes, however, spongiform changes of cortical layers II and III are present in the TEMPORAL LOBE and FRONTAL LOBE. (From Brain 1998 Jan;121(Pt 1):115-26)"
        :comment "http://purl.bioontology.org/ontology/MESH/D018888")
(refine ProgressiveAphasia
        :comment "Aphasia that progresses with time"
        :comment "http://snomed.info/id/230278002")
(refine ChronicPostStrokeAphasia
        :comment "Aphasia that lasts after a stroke")
(refine TreadmillTraining
        :comment "Using a treadmill to excersise as a therapeutic regime"
        :comment "http://snomed.info/id/113131004")
(refine OccupationalTherapist
        :comment "Professionals trained to help individuals develop or regain skills needed to achieve independence in their lives."
        :comment "http://purl.bioontology.org/ontology/MESH/D000072087")
(refine PhysioTherapist
        :comment "Persons trained in PHYSICAL THERAPY SPECIALTY to make use of PHYSICAL THERAPY MODALITIES to prevent, correct, and alleviate movement dysfunction."
        :comment "http://purl.bioontology.org/ontology/MESH/D059825")
(refine NursePhysioTherapist
        :comment "Persons who, under the supervision of licensed PHYSICAL THERAPISTS, provide patient treatment using various PHYSICAL THERAPY TECHNIQUES"
        :comment "http://purl.bioontology.org/ontology/MESH/D063372")
(refine OccupationalTherapy
        :comment "Skilled treatment that helps individuals achieve independence in all facets of their lives. It assists in the development of skills needed for independent living."
        :comment "http://purl.bioontology.org/ontology/MESH/D009788")
(refine PhysicalTherapy
        :comment "Therapeutic modalities frequently used in PHYSICAL THERAPY SPECIALTY by PHYSICAL THERAPISTS or physiotherapists to promote, maintain, or restore the physical and physiological well-being of an individual."
        :comment "http://purl.bioontology.org/ontology/MESH/D026741")
(refine SpeechLanguageTherapy
        :comment "Therapeutic treatments for speech and language disorders  "
        :comment "http://snomed.info/id/311555007")
(refine ConstraintInducedLanguageTherapy
        :comment "Constraint Induced Language Therapy (CILT) is an aphasia treatment modeled after Constraint Induced Movement Therapy (CIMT). In CILT, compensatory non-verbal communication modalities are constrained and participants are required to make verbal requests and responses."
        :annotation (see-also "http://aphasiology.pitt.edu/2293/1/155-250-1-RV-Mozeiko.doc.pdf"))
(refine ConventionalPhysioTherapy
        :comment "A currently accepted and widely used physicaltherapy treatment for a certain type of disease, based on the results of past research. "
        :comment "derived from http://purl.obolibrary.org/obo/NCIT_C97149")
(refine DailyOccupationPerformanceCognitiveOrientation
        :comment "Cognitive Orientation to daily Occupational Performance (CO-OP; CO-OP Approach TM) is a performance-based treatment approach for children and adults who experience difficulties performing the skills they want to, need to or are expected to perform."
        :annotation (see-also "https://icancoop.org/pages/the-co-op-approach"))
(refine DailyOccupationPerformanceKnowledgeTranslation
        :comment "Knowledge translation related to health has been described as “a dynamic and iterative process that includes the synthesis, dissemination, exchange and ethically sound application of knowledge to improve health, provide more effective health services and products, and strengthen the health care system"
        :annotation (see-also "https://doi.org/10.1186/s12913-019-3971-y"))
(refine DailyOccupationPerformanceTraining
        :comment "It is a performance-based treatment that involoves training on the  skills needed to or are expected to perform."
        :comment "Derived from https://icancoop.org/pages/the-co-op-approach")

(save-ontology "ontology.omn" :omn)
(save-ontology "ontology.owl" :owl)

