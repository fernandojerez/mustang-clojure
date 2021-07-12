(ns org.babelkitt.mustang.api.request)

(defprotocol Request
  (get-param [this name] "Return path param value")
  (get-query-string [this name] "Return query string value")
  (get-uri [this] "return URI")
  (get-header [this header] "return header value"))