(ns org.babelkitt.utils-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.spec.test.alpha :as st]
            [org.babelkitt.utils :as utils]))

(st/instrument)

(deftest split-uri-test
  (testing "split uri without query string"
    (is (= ["/test", ""] (utils/split-uri "/test"))))
  (testing "split uri with query-string"
    (is (= ["/test", "name=value&test=10"] (utils/split-uri "/test?name=value&test=10")))))

(deftest extract-path-values-test
  (testing "no values in the path"
    (is (= [] (utils/extract-path-values "/test"))))
  (testing "with one path"
    (is (= [{:value "name" :value-type :path}] (utils/extract-path-values "/test/{name}")))))

(deftest extract-query-string-values-test
  (testing "no values in the path"
    (is (= [] (utils/extract-query-string-values "name=name&key=key")))
    (is (= [{:value "user-name" :name "name" :value-type :query-string}] (utils/extract-query-string-values "name={user-name}&lastName=lastName")))))

(deftest parse-uri-test
  (testing "No uri values"
    (is (= [] (utils/parse-uri "/prueba"))))
  (testing "Extract path values"
    (is (= [{:value "name" :value-type :path}] (utils/parse-uri "/prueba/{name}"))))
  (testing "Extract path and query-string values"
    (is (= [{:value "name" :value-type :path} {:value "id" :name "id" :value-type :query-string}] (utils/parse-uri "/user/{name}?id={id}")))))


(parse-uri-test)