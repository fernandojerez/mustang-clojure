(ns org.babelkitt.mustang.armeria
  (:require
   [org.babelkitt.mustang.utils :as u]
   [org.babelkitt.mustang.api.request :as r])
  (:import [com.linecorp.armeria.server Server ServerBuilder HttpService ServiceRequestContext]
           [com.linecorp.armeria.common SessionProtocol HttpRequest HttpResponse QueryParams]))


(defrecord ArmeriaRequest [^ServiceRequestContext ctx ^HttpRequest request ^QueryParams query-string]
  r/Request
  (get-param [_ name] (.pathParam ctx name))
  (get-query-string [_ name] (.get query-string name))
  (get-uri [_] (-> (.uri request) (.toASCIIString)))
  (get-header [_ name] (-> (.headers request) (.get name))))

(defn create-http-service
  "Create the HttpService"
  [route]
  (reify com.linecorp.armeria.server.HttpService
    (^HttpResponse serve [this ^ServiceRequestContext ctx ^HttpRequest request]
      (let [query-string (second (u/split-uri (.toASCIIString (.uri request))))
            armeria-request (->ArmeriaRequest ctx request (QueryParams/fromQueryString query-string))]
        (cast HttpResponse ((.callback route) armeria-request))))))

(defn to-armeria-port
  [protocol]
  (case protocol
    :http (SessionProtocol/HTTP)
    :https (SessionProtocol/HTTPS)))

(defn register-port
  "register a port in the service builder"
  [^ServerBuilder builder port]
  (.port builder (:port port)
         (into-array SessionProtocol
                     (map to-armeria-port (or (:protocols port) [:http])))))

(defn register-route
  "Register a route as a service in armeria"
  [^ServerBuilder builder route]
  (let [^HttpService service (create-http-service route)
        ^String path (:path route)]
    (.service builder path
              service)))

(defn start-server
  "Starts a server"
  [config]
  (let [builder (Server/builder)]
    (doseq [port (:ports config)]
      (register-port builder port))
    (doseq [route (:routes config)]
      (case (.route-type route)
        :rest (register-route builder route)))
    (let [server (.build builder)
          start-future (.start server)]
      (future (.join start-future) server))))

(defn stop-server
  "Stop the server"
  [^Server server]
  (let [stop-future (.stop server)]
    (future (.join stop-future) nil)))