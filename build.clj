(ns build)

(def jar-name "mustang-clojure.jar")
(def target-classes "target/classes")
(def src-path "src")
(def test-path "test")
(def namespaces
  [])

(defn ^:private exec-target
  [target & args]
  ((eval (symbol "clojure.core/require")) (symbol (namespace target)))
  (apply (eval (symbol target)) args))

(defn compile-target
  "Target to compile clojure with AOT for integration with armeria"
  [_]
  (.mkdirs (java.io.File. target-classes))
  (binding [*compile-path* target-classes]
    (doseq [n namespaces]
      (compile n))))

(defn jar-target
  "Package the library into a jar"
  [_]
  (exec-target 'hf.depstar/jar
               {:aot true
                :jar jar-name
                :compile-ns namespaces}))

(defn coverage-target
  "Run tests and coverage report"
  [_]
  (compile-target _)
  (exec-target 'cloverage.coverage/run-project
               {:src-ns-path  [src-path]
                :test-ns-path [test-path]}))
