(ns org.babelkitt.mustang.api.request-specs
  (:require
   [clojure.spec.alpha :as s]
   [org.babelkitt.mustang.api.request :as r]))

(s/fdef r/get-header
  :args (s/cat :request #(satisfies? r/Request) :header-name string?)
  :ret (s/nilable string?))

(s/fdef r/get-param
  :args (s/cat :request #(satisfies? r/Request) :header-name string?)
  :ret (s/nilable string?))

(s/fdef r/get-query-string
  :args (s/cat :request #(satisfies? r/Request) :header-name string?)
  :ret (s/nilable string?))

(s/fdef r/get-uri
  :args (s/cat :request #(satisfies? r/Request))
  :ret  string?)