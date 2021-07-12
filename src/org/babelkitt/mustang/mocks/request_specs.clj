(ns org.babelkitt.mustang.mocks.request-specs
  (:require
   [clojure.spec.alpha :as s]
   [org.babelkitt.mustang.mocks.request :as m]))

; RequestMock
(s/def ::mock-map (s/map-of string? string?))
(s/def ::params ::mock-map)
(s/def ::query-string ::mock-map)
(s/def ::headers ::mock-map)
(s/def ::uri string?)
(s/def ::RequestMock (s/keys :req-un [::params ::query-string ::headers ::uri]))

; RequestMockBuilder
(s/def ::request-builder-key #{:uri :params :query-string :headers})
(s/def ::RequestMockBuilder (s/map-of ::request-builder-key
                                      (s/or :uri string?
                                            :map ::mock-map)))

(s/fdef m/builder
  :args (s/cat :uri ::uri)
  :ret ::RequestMockBuilder)

(s/fdef m/set-params
  :args (s/cat :request ::RequestMockBuilder :params ::params)
  :ret ::RequestMockBuilder)

(s/fdef m/set-query-string
  :args (s/cat :request ::RequestMockBuilder :query-string ::query-string)
  :ret ::RequestMockBuilder)

(s/fdef m/set-headers
  :args (s/cat :request ::RequestMockBuilder :headers ::headers)
  :ret ::RequestMockBuilder)

(s/fdef m/build
  :args (s/cat :request ::RequestMockBuilder)
  :ret ::RequestMock)