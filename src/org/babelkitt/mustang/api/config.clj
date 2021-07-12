(ns org.babelkitt.mustang.api.config)

(defrecord Port [port tls protocol])
(defrecord TlsConfig [certificate private-key pass-phrase])
(defrecord Config [ports routes])