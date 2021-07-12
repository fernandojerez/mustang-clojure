(ns org.babelkitt.mustang.api.route
  (:require
   [org.babelkitt.mustang.api.request :as r]
   [org.babelkitt.mustang.utils :as u]))

(defrecord Route [route-type path callback])

(defn process-uri-value
  [uri-value]
  (let [{:keys [name, value, value-type]} uri-value
        variable-name (symbol (str "param-" value))]
    (case value-type
      :path `[~variable-name (r/get-param ~'request ~value)]
      :query-string `[~variable-name (r/get-query-string ~'request ~name)])))

(defn process-uri-values
  [path]
  (let [uri-values (u/parse-uri path)]
    (reduce #(into % (process-uri-value %2)) [] uri-values)))

(defmacro route
  [uri & callback]
  (let [path (first (u/split-uri uri))]
    `(let [callback# (fn [~'request] (let* ~(process-uri-values uri) ~@callback))]
       (->Route :rest ~path callback#))))