(ns org.babelkitt.mustang.mocks.request-test
  (:require [clojure.test :refer [deftest is testing]]
            [orchestra.spec.test :as st]
            [org.babelkitt.mustang.mocks.request :as mocks]
            [org.babelkitt.mustang.api.request :as m]))

(require 'org.babelkitt.mustang.api.request-specs)
(require 'org.babelkitt.mustang.mocks.request-specs)
(st/instrument)

(deftest request-builder-test
  (testing "create a request-builder with only uri"
    (is (= (mocks/map->RequestMock {:uri "/test/name", :headers {}, :params {}, :query-string {}})
           (-> (mocks/builder "/test/name")
               (mocks/build)))))
  (testing "create a request builder complete"
    (is (= (mocks/map->RequestMock
            {:uri "/test/name"
             :headers {"Content-Type" "text/plain"}
             :params {"name" "heli"}
             :query-string {"filterBy" "name"}})
           (-> (mocks/builder "/test/name")
               (mocks/set-params {"name" "heli"})
               (mocks/set-query-string {"filterBy" "name"})
               (mocks/set-headers {"Content-Type" "text/plain"})
               (mocks/build))))))

(deftest request-mock-test
  (let [mock (-> (mocks/builder "/test/name")
                 (mocks/set-params {"name" "heli"})
                 (mocks/set-query-string {"filterBy" "name"})
                 (mocks/set-headers {"Content-Type" "text/plain"})
                 (mocks/build))]
    (testing "check mock values"
      (is (= "/test/name" (m/get-uri mock)))
      (is (= "heli" (m/get-param mock "name")))
      (is (= "name" (m/get-query-string mock "filterBy")))
      (is (= "text/plain" (m/get-header mock "Content-Type"))))))
