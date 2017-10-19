(ns clojure-snippets.core
  (:gen-class))


(require 'clojure.math.numeric-tower)
(require 'clojure.string)


                                        ; numbers


(class 1)
(class 1N)
(class 1M)


(bigint 1)
(bigdecimal 1)


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
                  (str "%." precision \f) ; use host locale (decimal-sep, thousands-sep)
                  (bigdec x))))


                                        ; booleans


(boolean "12.34") ; see truthiness of a value
(boolean nil)
(boolean false)

  
                                        ; dates


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
