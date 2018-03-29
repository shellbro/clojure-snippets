(defproject clojure-snippets "0.2.0"
  :description "This is my Clojure playground and cheatsheet containing
variety of code examples, language idioms, utility functions and recipes
that I find worth of writing down somewhere."
  :url "https://github.com/shellbro/clojure-snippets"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[clj-time "0.14.2"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]]
  :main ^:skip-aot clojure-snippets.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
