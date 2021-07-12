(ns org.babelkitt.mustang.mocks.request
  (:require
   [org.babelkitt.mustang.api.request :as m]))

(defrecord RequestMock [uri headers params query-string]
  m/Request
  (get-param [_ name] (get params name))
  (get-query-string [_ name] (get query-string name))
  (get-uri [_] uri)
  (get-header [_ name] (get headers name)))

(defn builder
  "Create a request mock builder"
  [uri]
  {:uri uri})

(defn set-params
  "Set params to request mock"
  [mock-builder params]
  (assoc mock-builder :params params))

(defn set-query-string
  "Set query string to request mock"
  [mock-builder query-string]
  (assoc mock-builder :query-string query-string))

(defn set-headers
  "Set headers to request mock"
  [mock-builder headers]
  (assoc mock-builder :headers headers))

(defn build
  "Create a Mock Request"
  [mock-builder]
  (map->RequestMock (merge {:params {} :headers {} :query-string {}} mock-builder)))