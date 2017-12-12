(ns clojure-snippets.core
  (:require
   [clojure.math.numeric-tower]
   [clojure.string])
  (:gen-class))


                                        ; numbers - integers


(class 1) ; java.lang.Long
(class 1N) ; clojure.lang.BigInt
(bigint 1) ; 1N
(bigint "1") ; 1N
(integer? 1) ; true
(integer? 1N) ; true
(= 1 1N) ; true
(class (+ 1 1N)) ; clojure.lang.BigInt
(+ Long/MAX_VALUE 1) ; integer overflow
(+' Long/MAX_VALUE 1) ; 9223372036854775808N


(biginteger 1) ; 1
(biginteger "1") ; 1
(class (biginteger "1")) ; java.math.BigInteger


                                        ; numbers - doubles and exact numbers (ratios, decimals)


(class 0.1) ; java.lang.Double
(class 0.1M) ; java.math.BigDecimal
(bigdec 0.1) ; 0.1M
(bigdec "0.1") ; 0.1M
(decimal? 0.1) ; false
(decimal? 0.1M) ; true
(= 0.1 0.1M) ; false
(class (+ 0.1M 0.1)) ; java.lang.Double


(ratio? 1.5M) ; false
(ratio? (/ 3 2)) ; true
(class (+ (/ 3 2) 0.1)) ; java.lang.Double
(class (+ (/ 3 2) 0.1M)) ; java.math.BigDecimal
(class (+ 0.1M (/ 3 2))) ; java.math.BigDecimal


(class (clojure.math.numeric-tower/floor 1.5)) ; java.lang.Double
(class (clojure.math.numeric-tower/floor 1.5M)) ; clojure.lang.BigInt
(class (clojure.math.numeric-tower/ceil 1.5)) ; java.lang.Double
(class (clojure.math.numeric-tower/ceil 1.5)) ; clojure.lang.BigInt


(class (clojure.math.numeric-tower/abs 1)) ; java.lang.Long
(class (clojure.math.numeric-tower/abs Long/MIN_VALUE)) ; clojure.lang.BigInt
(class (clojure.math.numeric-tower/round 1.5)) ; java.lang.Long
(class (clojure.math.numeric-tower/round 1.5M)) ; clojure.lang.BigInt


(defn round2
  "Round a double to the given precision (number of fractional digits)"
  [d precision]
  (let [factor (Math/pow 10 precision)]
    (/ (Math/round (* d factor)) factor)))


                                        ; strings


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
