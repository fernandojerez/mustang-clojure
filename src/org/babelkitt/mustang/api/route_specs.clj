(ns org.babelkitt.mustang.api.route-specs
  (:require
   [clojure.spec.alpha :as s]
   [org.babelkitt.mustang.utils-specs :as u]
   [org.babelkitt.mustang.api.route :as m]))


; Route
(s/def ::path string?)
(s/def ::callback fn?)
(s/def ::route-type #{:rest})
(s/def ::Route (s/keys :req-un [::route-type] :opt-un [::path ::callback ::grpc-classes]))

; process-uri-value
(s/def ::process-uri-value-ret (s/cat :variable symbol? :get-value seq?))
(s/fdef m/process-uri-value
  :args (s/cat :uri-value ::u/uri-value)
  :ret ::process-uri-value-ret)

; process-uri-values 
(s/fdef m/process-uri-values
  :args (s/cat :path string?)
  :ret (s/and vector? (s/* ::process-uri-value-ret)))

; route macro
(s/fdef m/route
  :args (s/cat :path string? :clauses (s/+ any?))
  :ret ::Route)