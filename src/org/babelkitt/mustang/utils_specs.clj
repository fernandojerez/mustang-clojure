(ns org.babelkitt.mustang.utils-specs
  (:require
   [clojure.spec.alpha :as s]
   [org.babelkitt.mustang.utils :as u]))

(defmacro validate-spec
  "Create a partial to validate a spec"
  [spec]
  `(partial s/valid? ~spec))

(s/def ::value-type #{:path :query-string})
(s/def ::value string?)
(s/def ::name string?)
(s/def ::uri-value (s/keys :req-un [::value ::value-type] :opt-un [::name]))
(s/def ::split-uri-ret (s/cat :path string? :query-string string?))

(s/fdef u/split-uri
  :args (s/cat :uri string?)
  :ret ::split-uri-ret)

(s/fdef u/extract-path-values
  :args (s/cat :path string?)
  :ret (s/* ::uri-value))

(s/fdef u/extract-query-string-values
  :args (s/cat :query-string string?)
  :ret (s/* ::uri-value))

(s/fdef u/parse-uri
  :args (s/cat :uri string?)
  :ret (s/* ::uri-value))
