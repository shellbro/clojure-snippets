(ns clojure-snippets.utils-pure
  (:require [clj-time.format]
            [clojure.math.numeric-tower :as numeric-tower]
            [clojure.string :as string])
  (:gen-class))

(defn in?
  "True if coll contains x. Works with nil and false"
  [coll x]
  (some #(= x %) coll))

(defn int2
  "Like int but supports strings"
  [x]
  (if (string? x)
    (Integer/parseInt x)
    (int x)))

(defn ceil [x]
  (-> x
      Math/ceil
      int))

(defn floor [x]
  (-> x
      Math/floor
      int))

(defn round2
  "Round to the given precision (number of fractional digits)"
  [x precision]
  (let [factor (numeric-tower/expt 10 precision)]
    (-> x
        (* factor)
        numeric-tower/round
        (/ factor)
        bigdec)))

(defn bigdec-locale
  "Helper function essentially wrapping plain bigdec (that uses ROOT locale)
  with localization support"
  ([s] (bigdec-locale s "." ""))
  ([s decimal-sep thousands-sep]
   (let [ds (str decimal-sep) ts (str thousands-sep)]
     (-> s
         (string/replace ds ".")
         (string/replace ts "")
         bigdec))))

(defn bigdec-en-us [s] (bigdec-locale s \. \,))

(defn bigdec-pl-pl [s] (bigdec-locale s \, \space))

(defn str-root-locale
  ([d] (str-root-locale d 2))
  ([d precision]
   (let [f (str "%." precision "f")
         d (to-array [d])]
     (String/format java.util.Locale/ROOT f d))))

(defn str-host-locale
  ([x] (str-host-locale x 2))
  ([x precision]
   (let [f (str "%." precision "f")]
     (format f (bigdec x)))))

(defn re-escape [s]
  (java.util.regex.Pattern/quote s))

(defmulti normalize (fn [x]
                      (cond
                        (string? x) :string
                        (seqable? x) :seqable)))

(defmethod normalize :string [s]
  (-> s
      (string/replace #"\s+" " ")
      string/trim
      string/capitalize))

(defmethod normalize :seqable [coll]
  (map normalize coll))

(defn str-iso->date [s]
  (clj-time.format/parse (clj-time.format/formatters :year-month-day) s))

(defn str-pl-pl->date [s]
  (clj-time.format/parse (clj-time.format/formatter "dd.MM.YYYY") s))

(defn date->str-iso [d]
  (clj-time.format/unparse (clj-time.format/formatters :year-month-day) d))

(defn date->str-pl-pl [d]
  (clj-time.format/unparse (clj-time.format/formatter "dd.MM.YYYY") d))
