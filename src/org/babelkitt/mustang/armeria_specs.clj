(ns org.babelkitt.mustang.armeria-specs
  (:require
   [clojure.spec.alpha :as s]
   [org.babelkitt.mustang.armeria :as c]
   [org.babelkitt.mustang.api.config-specs :as m]
   [org.babelkitt.mustang.api.route-specs :as r])
  (:import
   [com.linecorp.armeria.server Server ServerBuilder HttpService]
   [com.linecorp.armeria.common SessionProtocol]))

(s/def ::ServerBuilder #(instance? ServerBuilder))
(s/def ::HttpService #(instance? HttpService))
(s/def ::Server #(instance? Server))
(s/def ::SessionProtocol #(instance? SessionProtocol))

(s/fdef c/create-http-service
  :args (s/cat :route ::r/Route)
  :ret ::HttpService)

(s/fdef c/to-armeria-port
  :args (s/cat :protocol ::m/port)
  :ret ::SessionProtocol)

(s/fdef c/register-port
  :args (s/cat :service-builder ::ServerBuilder :port ::m/Port)
  :ret ::ServerBuilder)

(s/fdef c/register-route
  :args (s/cat :service-builder ::ServerBuilder :route ::r/Route)
  :ret ::ServerBuilder)

(s/fdef c/start-server
  :args (s/cat :config ::m/Config)
  :ret future?)

(s/fdef c/stop-server
  :args (s/cat :server ::Server)
  :ret future?)