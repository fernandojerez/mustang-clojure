(ns org.babelkitt.mustang.api.config-specs
  (:require
   [clojure.spec.alpha :as s]
   [org.babelkitt.mustang.utils-specs :as u]
   [org.babelkitt.mustang.api.route-specs :as r])
  (:import [java.nio.file Paths Files LinkOption]))

(defn valid-port?
  "Validate port range"
  [port]
  (and (>= port 0) (<= port 65353)))

(defn valid-path?
  "Test that the path is an existing file"
  [path]
  (Files/exists
   (Paths/get "" (into-array [path]))
   (into-array LinkOption [])))

; TlsConfig
(s/def ::certificate valid-path?)
(s/def ::private-key valid-path?)
(s/def ::pass-phrase string?)
(s/def ::TlsConfig (s/keys :req-un [::certificate ::private-key ::pass-phrase]))

; Port
(s/def ::port (s/and int? valid-port?))
(s/def ::tls (s/nilable (u/validate-spec ::TlsConfig)))
(s/def ::protocol #{:http :https})
(s/def ::protocols (s/coll-of ::protocol))
(s/def ::Port (s/keys :req-un [::port] :opt-un [::tls ::protocols]))

; Config
(s/def ::ports (s/+ ::Port))
(s/def ::routes (s/+ ::r/Route))
(s/def ::Config (s/keys :req-un [::ports ::routes]))