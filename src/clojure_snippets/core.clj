(ns clojure-snippets.core
  (:require
   [clj-time.format]
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


                                        ; numbers - exact numbers (ratios, decimals) and doubles
                                        ; WARNING: 1/3 can't be expressed exactly
                                        ; in a base 2 (double) or in a base 10 (decimal)
                                        ; but decimal throws an exception


(class (/ 1 3)) ; clojure.lang.Ratio
(class (/ 1N 3)) ; clojure.lang.Ratio
(rationalize 0.3) ; 3/10
(rationalize 0.3M) ; 3/10
(class (rationalize 0.3)) ; clojure.lang.Ratio
(ratio? (/ 1 3)) ; true
(ratio? (/ 1 1)) ; false
(= (/ 3 2) 1.5M) ; false
(class (+ (/ 1 3) 0.1M)) ; Non-terminating decimal expansion; no exact representable decimal result.
(class (+ (/ 3 2) 0.1M)) ; java.math.BigDecimal
(class (+ (/ 1 3) 0.1)) ; java.lang.Double


(class 0.1) ; java.lang.Double
(class 0.1M) ; java.math.BigDecimal
(bigdec 0.1) ; 0.1M
(bigdec "0.1") ; 0.1M
(bigdec (/ 1 3)) ; Non-terminating decimal expansion; no exact representable decimal result.
(with-precision 2 (bigdec (/ 1 3))) ; 0.33M
(decimal? 0.1) ; false
(decimal? 0.1M) ; true
(= 0.1 0.1M) ; false
(class (+ 0.1M 0.1)) ; java.lang.Double
(class (+ 0.1M 0.1M)) ; java.math.BigDecimal
(class (* 0.1M 0.1)) ; java.lang.Double
(class (* 0.1M 0.1M)) ; java.math.BigDecimal
(class (/ 0.1M 0.1)) ; java.lang.Double
(class (/ 0.1M 0.1M)) ; java.math.BigDecimal
(class (/ 1M 3M)) ; Non-terminating decimal expansion; no exact representable decimal result.
(with-precision 2 (/ 1M 3M)) ; 0.33M


(+ 0.1 0.2) ; 0.30000000000000004


(class (clojure.math.numeric-tower/floor 1)) ; java.lang.Long
(class (clojure.math.numeric-tower/floor 1N)) ; java.lang.BigInt
(class (clojure.math.numeric-tower/floor (/ 1 3))) ; java.lang.BigInt
(class (clojure.math.numeric-tower/floor 0.1)) ; java.lang.Double
(class (clojure.math.numeric-tower/floor 0.1M)) ; clojure.lang.BigInt


(class (clojure.math.numeric-tower/ceil 1)) ; java.lang.Long
(class (clojure.math.numeric-tower/ceil 1N)) ; java.lang.BigInt
(class (clojure.math.numeric-tower/ceil (/ 1 3))) ; clojure.lang.BigInt
(class (clojure.math.numeric-tower/ceil 0.1)) ; java.lang.Double
(class (clojure.math.numeric-tower/ceil 0.1M)) ; clojure.lang.BigInt


(class (clojure.math.numeric-tower/round 1)) ; java.lang.Long
(class (clojure.math.numeric-tower/round 1N)) ; java.lang.BigInt
(class (clojure.math.numeric-tower/round (/ 1 3))) ; clojure.lang.BigInt
(class (clojure.math.numeric-tower/round 0.1)) ; java.lang.Long
(class (clojure.math.numeric-tower/round 0.1M)) ; clojure.lang.BigInt


(class (clojure.math.numeric-tower/abs 1)) ; java.lang.Long
(class (clojure.math.numeric-tower/abs Long/MIN_VALUE)) ; clojure.lang.BigInt
(class (clojure.math.numeric-tower/abs 1N)) ; clojure.lang.BigInt
(class (clojure.math.numeric-tower/abs (/ 1 3))) ; clojure.lang.Ratio
(class (clojure.math.numeric-tower/abs 0.1)) ; java.lang.Double
(class (clojure.math.numeric-tower/abs 0.1M)) ; java.math.BigDecimal


(defn round2
  "Round to the given precision (number of fractional digits)"
  [d precision]
  (let [factor (clojure.math.numeric-tower/expt 10 precision)]
    (bigdec (/ (clojure.math.numeric-tower/round (* d factor))
               factor))))


(defn str->num
  ([s] (str->num s \, \space))
  ([s decimal-sep] (str->num s decimal-sep \space))
  ([s decimal-sep thousands-sep]
   (bigdec
    (clojure.string/replace
     (clojure.string/replace s decimal-sep \.) (str thousands-sep) ""))))


(defn num->str
  ([n] (num->str n 2))
  ([n precision] (format (str "%." precision \f) ; use host locale: decimal-sep, thousands-sep
                         (bigdec n))))


(defn d->str [d precision]
  (java.lang.String/format java.util.Locale/ROOT
                           (str "%." precision "f")
                           (to-array [d])))


                                        ; strings


(str 12.34M) ; cast anything to String
(str 1 "foo" \a) ; append/prepend to a String


(format "%.2f" (/ 1 3)) ; f != clojure.lang.Ratio
(format "%.2f" 0.005M) ; "0.01"
(format "%.2f" 0.005) ; "0.01"


                                        ; booleans


                                        ; see truthiness of a value
(boolean "foo") ; true
(boolean 0) ; true
(boolean '()) ; true
(boolean nil) ; false
(boolean false) ; false


                                        ; dates


(defn str->date [s]
  (clj-time.format/parse (clj-time.format/formatter "dd.MM.YYYY") s))


(defn date->str [d]
  (clj-time.format/unparse (clj-time.format/formatters :year-month-day) d))


                                        ; io


(defn lines [f]
  (clojure.string/split-lines (slurp f)))


(binding [*out* *err*]
  (println "This text will be printed to STDERR"))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
