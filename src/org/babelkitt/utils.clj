(ns org.babelkitt.utils
  (:require
   [clojure.spec.alpha :as s]
   [org.babelkitt.specs.utils :as u])
  (:gen-class))


(def ^:private value-chars "([a-zA-Z\\-\\.\\>]+)")
(def ^:private path-fragment (re-pattern (str "\\{" value-chars "\\}")))
(def ^:private query-string (re-pattern (str value-chars "=\\{" value-chars "\\}")))
(def ^:private query-string-separator #"&")

(s/fdef split-uri
  :args (s/cat :uri string?)
  :ret (s/cat :path string? :query-string string?))

(defn split-uri
  "split the uri in path and query-string"
  [uri]
  (let [index (clojure.string/index-of uri "?")]
    (if index
      [(subs uri 0 index) (subs uri (inc index))]
      [uri ""])))

(s/fdef extract-path-values
  :args (s/cat :path string?)
  :ret (s/* ::s/uri-value))

(defn extract-path-values
  [path]
  (map (fn [match] {:value (second match) :value-type :path})
       (re-seq path-fragment path)))

(s/fdef extract-query-string-values
  :args (s/cat :query-string string?)
  :ret (s/* ::s/uri-value))

(defn extract-query-string-values
  [query-string-value] (map (fn [[[_ name value]]] {:value value
                                                    :name name
                                                    :value-type :query-string})
                            (filter #(not (nil? %))
                                    (map
                                     (partial re-seq query-string)
                                     (clojure.string/split query-string-value query-string-separator)))))


(s/fdef parse-uri
  :args (s/cat :uri string?)
  :ret (s/* ::s/uri-value))

(defn parse-uri
  [uri]
  (let [[path query-string] (split-uri uri)]
    (concat [] (extract-path-values path) (extract-query-string-values query-string))))