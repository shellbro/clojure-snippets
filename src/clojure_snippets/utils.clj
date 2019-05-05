(ns clojure-snippets.utils
  (:require [clj-time.local]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string])
  (:gen-class))

(defn log [h & m]
  (binding [*out* *err*]
    (println
     (apply str (clj-time.local/local-now) " [" h "] " m))))

(defn exit
  ([status]
   (System/exit status))
  ([status msg]
   (if (> status 0)
     (binding [*out* *err*]
       (println msg))
     (println msg))
   (exit status)))

(defn lines [f]
  (string/split-lines (slurp f)))

(defn uuid []
  (.toString (java.util.UUID/randomUUID)))

(defn working-dir []
  (System/getProperty "user.dir"))

(defn dirname [path]
  (-> path
      io/file
      .getAbsoluteFile
      .getParent))

(defn jar-path
  "Utility function to get the path of jar in which this function is invoked"
  [& [ns]]
  (-> (or ns (class *ns*))
      .getProtectionDomain
      .getCodeSource
      .getLocation
      .getPath))

(defn lein-project-path
  "Utility function to get the project's directory path when using
  lein repl or lein run"
  []
  (working-dir))

(defn run-from-lein? []
  (string/includes? (jar-path) "/.m2/repository/org/clojure/clojure/"))

(defn config-path []
  (if (run-from-lein?)
    (str (lein-project-path) "/configs/local.edn")
    (str (jar-path) "/" "config.edn")))

(defn read-config [path]
  (edn/read-string (slurp path)))
