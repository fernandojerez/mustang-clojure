(ns org.babelkitt.specs.utils
  (:require
   [clojure.spec.alpha :as s])
  (:gen-class))

(s/def ::value-type #{:path :query-string})
(s/def ::value string?)
(s/def ::name string?)
(s/def ::uri-value (s/keys :req-un [::value ::value-type] :opt-un [::name]))