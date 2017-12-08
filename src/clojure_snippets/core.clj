(ns clojure-snippets.core
  (:require
   [clojure.math.numeric-tower]
   [clojure.string])
  (:gen-class))

                                        ; numbers


(class 1)


(class 1N)
(bigint 1)
(integer? 1N)


(class 1M)
(bigdec 1)
(decimal? 1M)


(defn round2
  "Round a double to the given precision (number of fractional digits)"
  [d precision]
  (let [factor (Math/pow 10 precision)]
    (/ (Math/round (* d factor)) factor)))


(clojure.math.numeric-tower/round (/ 3 2))


(clojure.math.numeric-tower/abs Long/MIN_VALUE)


                                        ; strings


(bigdec "12.34") ; parse String (or anything) into Number (BigDecimal)


(str 12.34M) ; cast anything to String


(str 1 "foo" \a) ; append/prepend to a String


(defn str->num
  ([s] (str->num s \, \space))
  ([s decimal-sep] (str->num s decimal-sep \space))
  ([s decimal-sep thousands-sep]
   (bigdec
    (clojure.string/replace
     (clojure.string/replace s decimal-sep \.) (str thousands-sep) ""))))


(defn num->str
  ([x] (num->str x 2))
  ([x precision] (format
                  (str "%." precision \f) ; use host locale: decimal-sep, thousands-sep
                  (bigdec x))))


                                        ; booleans


(boolean "12.34") ; see truthiness of a value
(boolean nil)
(boolean false)


                                        ; dates


                                        ; io


(defn lines [f]
  (clojure.string/split-lines (slurp f)))


(binding [*out* *err*]
  (println "This text will be printed to STDERR"))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
