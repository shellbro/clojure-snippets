(ns clojure-snippets.utils
  (:require [clj-time.format]
            [clojure.math.numeric-tower :as math]
            [clojure.string :as string])
  (:gen-class))

(defn lines [f]
  (string/split-lines (slurp f)))

(defn round2
  "Round to the given precision (number of fractional digits)"
  [x precision]
  (let [factor (math/expt 10 precision)]
    (-> (* x factor)
        math/round
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
   (let [f (str "%." precision "f") d (to-array [d])]
     (java.lang.String/format java.util.Locale/ROOT f d))))

(defn str-host-locale
  ([x] (str-host-locale x 2))
  ([x precision]
   (let [f (str "%." precision "f")]
     (format f (bigdec x)))))

(defn str->iso-date [s]
  (clj-time.format/parse (clj-time.format/formatters :year-month-day) s))

(defn str->pl-pl-date [s]
  (clj-time.format/parse (clj-time.format/formatter "dd.MM.YYYY") s))

(defn iso-date->str [d]
  (clj-time.format/unparse (clj-time.format/formatters :year-month-day) d))

(defn pl-pl-date->str [d]
  (clj-time.format/unparse (clj-time.format/formatter "dd.MM.YYYY") d))

(comment
  (round2 1 2) ; 1M
  (round2 1N 2) ; 1M
  (round2 1/3 2) ; 0.33M
  (round2 0.005M 2) ; 0.01M
  (round2 0.005 2) ; 0.01M

  (bigdec-en-us "0,125") ; 125M
  (bigdec-en-us "0.125") ; 0.125M

  (bigdec-pl-pl "0,125") ; 0.125M
  (bigdec-pl-pl "0.125") ; 0.125M

  (str-root-locale 0.005M) ; "0.01"
  (str-root-locale 0.005) ; "0.01"
  (str-root-locale 0.5M) ; "0.50"
  (str-root-locale 0.5) ; "0.50"

  (str-host-locale 1) ; "1.00"
  (str-host-locale 1N) ; "1.00"
  (str-host-locale 1/8) ; "0.13"
  (str-host-locale 0.005M) ; "0.01"
  (str-host-locale 0.005) ; "0.01"
  (str-host-locale 0.5M) ; "0.50"
  (str-host-locale 0.5)) ; "0.50"
