(ns org.babelkitt.mustang.api.route-test
  (:require [clojure.test :refer [deftest is testing]]
            [orchestra.spec.test :as st]
            [org.babelkitt.mustang.api.route :as m]
            [org.babelkitt.mustang.api.request :as r]
            [org.babelkitt.mustang.mocks.request :as request]))

(require 'org.babelkitt.mustang.api.route-specs)
(st/instrument)

(defn execute_route
  [route request]
  ((.callback route) request))

(deftest process-uri-value-test
  (testing "test path value"
    (is (= `[~'param-id (r/get-param ~'request "id")]
           (m/process-uri-value {:value "id" :value-type :path}))))
  (testing "test query string value"
    (is (= `[~'param-id (r/get-query-string ~'request "user-id")]
           (m/process-uri-value {:value "id" :value-type :query-string :name "user-id"})))))

(deftest process-uri-values-test
  (testing "test path without values"
    (is (= []
           (m/process-uri-values "/user/create"))))
  (testing "test path with values"
    (is (= `[~'param-id (r/get-param ~'request "id")
             ~'param-filter-column (r/get-query-string ~'request "filterBy")]
           (m/process-uri-values "/user/{id}?filterBy={filter-column}")))))

(deftest route-test
  (let [mock (-> (request/builder "/test/user/10")
                 (request/set-params {"user-id" "10"})
                 (request/build))]
    (testing "test route macro"
      (is (= "10" (execute_route
                   (m/route "/test/user/{user-id}" param-user-id)
                   mock))))))
