(ns clojure-snippets.core
  (:gen-class))

                                        ; parse String (or anything) into Number (BigDecimal)
(bigdec "12.34")

                                        ; cast anything to String
(str 12.34M)

                                        ; append/prepend to a String
(str 1 "foo" \a)

                                        ; abs
(defn abs [n] (max n (- n)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
