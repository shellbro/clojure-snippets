(ns clojure-snippets.core
  (:require clj-time.format
            [clojure.math.numeric-tower :as nt]
            [clojure.string :as str])
  (:gen-class))

;;;; Real numbers
(number? 1) ; true
(number? 1N) ; true
(number? 1/3) ; true
(number? 0.1M) ; true
(number? 0.1) ; true

(defn round2
  "Round to the given precision (number of fractional digits)."
  [x precision]
  (let [factor (nt/expt 10 precision)]
    (-> (* x factor)
        nt/round
        (/ factor)
        bigdec)))
(comment
  (round2 1 2) ; 1M
  (round2 1N 2) ; 1M
  (round2 1/3 2) ; 0.33M
  (round2 0.005M 2) ; 0.01M
  (round2 0.005 2)) ; 0.01M

(class (+ 1 1N)) ; clojure.lang.BigInt
(class (+ 1N 1/3)) ; clojure.lang.Ratio
(comment
  (class (+ 1/3 0.1M))) ; Non-terminating decimal expansion; no exact ...
(class (with-precision 2 (+ 1/3 0.1M))) ; java.math.BigDecimal
(class (+ 0.1M 0.1)) ; java.lang.Double

(class (nt/floor 1)) ; java.lang.Long
(class (nt/floor 1N)) ; java.lang.BigInt
(class (nt/floor (/ 1 3))) ; java.lang.BigInt
(class (nt/floor 0.1)) ; java.lang.Double
(class (nt/floor 0.1M)) ; clojure.lang.BigInt

(class (nt/ceil 1)) ; java.lang.Long
(class (nt/ceil 1N)) ; java.lang.BigInt
(class (nt/ceil (/ 1 3))) ; clojure.lang.BigInt
(class (nt/ceil 0.1)) ; java.lang.Double
(class (nt/ceil 0.1M)) ; clojure.lang.BigInt

(class (nt/round 1)) ; java.lang.Long
(class (nt/round 1N)) ; java.lang.BigInt
(class (nt/round (/ 1 3))) ; clojure.lang.BigInt
(class (nt/round 0.1)) ; java.lang.Long
(class (nt/round 0.1M)) ; clojure.lang.BigInt

(class (nt/abs 1)) ; java.lang.Long
(class (nt/abs Long/MIN_VALUE)) ; clojure.lang.BigInt
(class (nt/abs 1N)) ; clojure.lang.BigInt
(class (nt/abs (/ 1 3))) ; clojure.lang.Ratio
(class (nt/abs 0.1)) ; java.lang.Double
(class (nt/abs 0.1M)) ; java.math.BigDecimal

;;; Real numbers - exact numbers
(rational? 1) ; true
(rational? 1N) ; true
(rational? 1/3) ; true
(rational? 0.1M) ; true
(rational? 0.1) ; false

;;; Real numbers - doubles
(double? 1.0) ; true
(double? 1.0M) ; false

(+ 0.1 0.2) ; 0.30000000000000004

;; Real numbers - exact numbers - integers
(integer? 1) ; true
(integer? 1N) ; true
(integer? 1/1) ; true
(integer? 1/3) ; false

(bigint "1") ; 1N

(class 1) ; java.lang.Long
(class 1N) ; clojure.lang.BigInt

(comment
  (+ Long/MAX_VALUE 1)) ; Integer overflow
(+ Long/MAX_VALUE 1N) ; 9223372036854775808N
(+' Long/MAX_VALUE 1) ; 9223372036854775808N
(quot 22 7) ; 3
(rem 22 7) ; 1

;; Real numbers - exact numbers - ratios
(ratio? 1/3) ; true
(ratio? 1/1) ; false

(class (rationalize 1)) ; clojure.lang.Ratio
(class (rationalize 0.3M)) ; clojure.lang.Ratio
(class (rationalize 0.3)) ; clojure.lang.Ratio

;; Real numbers - exact numbers - decimals
;; 1/3 can't be expressed exactly neighter in base 2 (double) nor in
;; base 10 (decimal) but decimal throws an exception
(decimal? 0.1M) ; true
(decimal? 0.1) ; false

(bigdec "0.1") ; 0.1M
(comment
  (bigdec 1/3)) ; Non-terminating decimal expansion; no exact ...
(with-precision 2 (bigdec 1/3)) ; 0.33M

(class 0.1M) ; java.math.BigDecimal
(class 0.1) ; java.lang.Double

(comment
  (/ 1M 3M)) ; Non-terminating decimal expansion; no exact ...
(with-precision 2 (/ 1M 3M)) ; 0.33M

;; Helper function wrapping bigdec (which uses ROOT locale)
(defn str->dec
  ([s] (str->dec s "." ""))
  ([s decimal-sep thousands-sep]
   (let [ds (str decimal-sep) ts (str thousands-sep)]
     (-> s
         (str/replace ds ".")
         (str/replace ts "")
         bigdec))))

(defn pl-pl-str->dec [s] (str->dec s \, \space))
(comment
  (pl-pl-str->dec "0,125") ; 0.125M
  (en-us-str->dec "0.125")) ; 0.125M

(defn en-us-str->dec [s] (str->dec s \. \,))
(comment
  (en-us-str->dec "0,125") ; 125M
  (en-us-str->dec "0.125")) ; 0.125M

;;;; Strings and characters
(str 12.34M) ; Cast anything to string
(str 1 "foo" \a) ; Append/prepend to a string

(comment
  (format "%.2f" 1)) ; f != java.lang.Long
(comment
  (format "%.2f" 1N)) ; f != clojure.lang.BigInt
(comment
  (format "%.2f" (/ 1 3))) ; f != clojure.lang.Ratio
(format "%.2f" 0.005M) ; "0.01"
(format "%.2f" 0.005) ; "0.01"

;; Use host locale
(defn x->loc-str
  ([x] (x->loc-str x 2))
  ([x precision]
   (let [f (str "%." precision "f")]
     (format f (bigdec x)))))
(comment
  (x->loc-str 1) ; "1.00"
  (x->loc-str 1N) ; "1.00"
  (x->loc-str 1/8) ; "0.13"
  (x->loc-str 0.005M) ; "0.01"
  (x->loc-str 0.005) ; "0.01"
  (x->loc-str 0.5M) ; "0.50"
  (x->loc-str 0.5)) ; "0.50"

;; Use ROOT locale
(defn d->str
  ([d] (d->str d 2))
  ([d precision]
   (let [f (str "%." precision "f") d (to-array [d])]
     (java.lang.String/format java.util.Locale/ROOT f d))))
(comment
  (d->str 0.005M) ; "0.01"
  (d->str 0.005) ; "0.01"
  (d->str 0.5M) ; "0.50"
  (d->str 0.5)) ; "0.50"

;;;; Booleans and nil
;; Check truthiness of a value
(boolean false) ; false
(boolean nil) ; false
(boolean 0) ; true
(boolean '()) ; true
(boolean "foo") ; true

;;;; Dates
(defn iso-str->date [s]
  (clj-time.format/parse (clj-time.format/formatters :year-month-day) s))

(defn pl-pl-str->date [s]
  (clj-time.format/parse (clj-time.format/formatter "dd.MM.YYYY") s))

(defn date->iso-str [d]
  (clj-time.format/unparse (clj-time.format/formatters :year-month-day) d))

(defn date->pl-pl-str [d]
  (clj-time.format/unparse (clj-time.format/formatter "dd.MM.YYYY") d))

;;;; Bytes

;;;; Sequences and Collections
[1 2 3] ; [1 2 3]
(vec (range 1 4)) ; [1 2 3]
(vector 1 2 3) ; [1 2 3]

'(1 2 3) ; (1 2 3)

#{5 1 3} ; #{1 3 5}
(set [5 1 3]) ; #{1 3 5}
(hash-set 5 1 3) ; #{1 3 5}
(sorted-set) ; #{}
(sorted-set 5 1 3) ; #{1 3 5}

{:c 3 :b 2 :a 1} ; {:c 3 :b 2 :a 1}
(hash-map :c 3 :b 2 :a 1) ; {:c 3 :b 2 :a 1}
(sorted-map) ; {}
(sorted-map :c 3 :b 2 :a 1) ; {:a 1 :b 2 :c 3}

(seq nil) ; nil
(seq []) ; nil
(seq "foo") ; (\f \o \o)
(seq? "foo") ; false
(seq? (seq "foo")) ; true

;; car
(first nil) ; nil
(first []) ; nil
(first '(5 1 3)) ; 5
(first #{5 1 3}) ; 1
(first {:c 3 :b 2 :a 1}) ; [:c 3]

;; cdr
;; next -> (seq (rest))
(rest nil) ; ()
(next nil) ; nil
(rest []) ; ()
(next []) ; nil
(rest [1]) ; ()
(next [1]) ; nil
(rest '(5 1 3)) ; (1 3)
(rest #{5 1 3}) ; (3 5)
(rest {:c 3 :b 2 :a 1}) ; ([:b 2] [:a 1])

;; cons
(cons 10 nil) ; (10)
(cons 10 []) ; (10)
(cons 10 '(5 1 3)) ; (10 5 1 3)
(cons 10 #{5 1 3}) ; (10 1 3 5)
(cons 10 {:a 5 :b 1 :c 3}) ; (10 [:a 5] [:b 1] [:c 3])

(conj nil 10 11 12) ; (12 11 10)
(conj [] 10 11 12) ; [10 11 12]
(conj '(5 1 3) 10 11 12) ; (12 11 10 5 1 3)
(conj #{5 1 3} 10 11 12) ; #{1 3 12 11 5 10}
(comment
  (conj {:a 5 :b 1 :c 3} :d 10 :e 11 :f 12) ) ; Don't know how to create ...
(conj {:a 5 :b 1 :c 3} [:d 10] [:e 11] [:f 12]) ; {:a 5 :b 1 :c 3 :d 10 ... }
(conj {:a 5 :b 1 :c 3} {:d 10 :e 11} {:f 12}) ; {:a 5 :b 1 :c 3 :d 10 ... } ???

(into nil [10 11 12]) ; (12 11 10)
(into nil {:d 10 :e 11 :f 12}) ; ([:f 12] [:e 11] [:d 10])
(into [] [10 11 12]) ; [10 11 12]
(into [] {:d 10 :e 11 :f 12}) ; [[:d 10] [:e 11] [:f 12]]
(into '(5 1 3) [10 11 12]) ; (12 11 10 5 1 3)
(into '(5 1 3) {:d 10 :e 11 :f 12}) ; ([:f 12] [:e 11] [:d 10] 5 1 3)
(into #{5 1 3} [10 11 12]) ; #{1 3 12 11 5 10}
(into #{5 1 3} {:d 10 :e 11 :f 12}) ; #{[:f 12] 1 [:d 10] 3 [:e 11] 5}
(into {:a 5 :b 1 :c 3} {:d 10 :e 11 :f 12}) ; {:a 5 :b 1 :c 3 :d 10 :e 11 :f 12}

                                        ; range
                                        ; repeat
                                        ; iterate
                                        ; take
                                        ; cycle
                                        ; interleave
                                        ; interpose
                                        ; str/join
                                        ; filter
                                        ; take-while
                                        ; drop-while
                                        ; split-at
                                        ; split-with
                                        ; every?
                                        ; some
                                        ; not-every?
                                        ; not-any?
                                        ; map
                                        ; reduce
                                        ; sort
                                        ; sort-by
                                        ; re-seq

;; vector specific
(comment
  (nil 0)) ; Can't call nil
([nil false "c"] 0) ; nil
([nil false "c"] 1) ; false
([nil false "c"] 2) "c"
(comment
  ([nil false "c"] 3)) ; Unhandled java.lang.IndexOutOfBoundsException
(comment
  ([nil false "c"] :foo)) ; Key must be integer

(get [nil false "c"] 0) ; nil
(get [nil false "c"] 1) ; false
(get [nil false "c"] 2) ; "c"
(get [nil false "c"] 3) ; nil
(get [nil false "c"] :foo) ; nil
(get [nil false "c"] :foo :not-found) ; :not-found

(contains? [nil false "c"] 0) ; true
(contains? [nil false "c"] 3) ; false
(contains? [nil false "c"] :foo) ; false

;; set specific
(comment
  (nil :a)) ; Can't call nil
(#{nil false "c"} nil) ; nil
(#{nil false "c"} false) ; false
(#{nil false "c"} "c") ; "c"
(#{nil false "c"} :c) ; nil

(get nil :a) ; nil
(get #{nil false "c"} nil) ; nil
(get #{nil false "c"} false) ; false
(get #{nil false "c"} "c") ; "c"
(get #{nil false "c"} :c) ; nil
(get #{nil false "c"} :c :not-found) ; :not-found

(contains? #{nil false "c"} nil) ; true
(contains? #{nil false "c"} :c) ; false

;; map specific
(comment
  (nil :a)) ; Can't call nil
({:a nil :b false "c" 1} :a) ; nil
({:a nil :b false "c" 1} :b) ; false
({:a nil :b false "c" 1} "c") ; 1
({:a nil :b false "c" 1} :c) ; nil

(:a nil) ; nil
(:a {:a nil :b false "c" 1}) ; nil
(:b {:a nil :b false "c" 1}) ; false
(comment
  ("c" {:a nil :b false "c" 1})) ; "java.lang.String cannot be cast to ..."
(:c {:a nil :b false "c" 1}) ; nil

(get nil :a) ; nil
(get {:a nil :b false "c" 1} :a) ; nil
(get {:a nil :b false "c" 1} :b) ; false
(get {:a nil :b false "c" 1} "c") ; 1
(get {:a nil :b false "c" 1} :c) ; nil
(get {:a nil :b false "c" 1} :c :not-found) ; :not-found

(contains? {:a nil :b false "c" 1} :a) ; true
(contains? {:a nil :b false "c" 1} :c) ; false

;;;; IO
(defn lines [f]
  (str/split-lines (slurp f)))

(binding [*out* *err*]
  (println "This text will be printed to STDERR"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
