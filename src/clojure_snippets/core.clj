(ns clojure-snippets.core
  (:gen-class))


                                        ; numbers

(defn abs [n] (max n (- n)))


                                        ; strings

(bigdec "12.34") ; parse String (or anything) into Number (BigDecimal)

(str 12.34M) ; cast anything to String

(str 1 "foo" \a) ; append/prepend to a String


                                        ; booleans

(boolean "12.34") ; see truthiness of a value
(boolean nil)
(boolean false)


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
