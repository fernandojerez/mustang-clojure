(ns org.babelkitt.mustang.utils
  (:require
   [clojure.string :as str]))

(def ^:private value-chars "([a-zA-Z\\-\\.\\>]+)")
(def ^:private path-fragment (re-pattern (str "\\{" value-chars "\\}")))
(def ^:private query-string (re-pattern (str value-chars "=\\{" value-chars "\\}")))
(def ^:private query-string-separator #"&")

(defn split-uri
  "split the uri returning the path and query-string"
  [uri]
  (let [index (str/index-of uri "?")]
    (if index
      [(subs uri 0 index) (subs uri (inc index))]
      [uri ""])))

(defn extract-path-values
  "find placeholders (values defined using {...}) into the path and returns a seq with that values"
  [path]
  (map (fn [match] {:value (second match) :value-type :path})
       (re-seq path-fragment path)))

(defn extract-query-string-values
  "find placeholders (values defined using {...}) into the query string and returns a seq with that values"
  [query-string-value] (map (fn [[[_ name value]]] {:value value
                                                    :name name
                                                    :value-type :query-string})
                            (filter #(not (nil? %))
                                    (map
                                     (partial re-seq query-string)
                                     (str/split query-string-value query-string-separator)))))

(defn parse-uri
  "Extract values from the url. The values are maps with :value, :name and :value-type keys"
  [uri]
  (let [[path query-string] (split-uri uri)]
    (concat [] (extract-path-values path) (extract-query-string-values query-string))))