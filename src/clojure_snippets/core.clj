(ns clojure-snippets.core
  (:gen-class))


(require 'clojure.string)


                                        ; numbers

(defn abs [n] (max n (- n)))


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
  ([n] (num->str n 2 \, \space))
  ([n precision] (num->str n precision \, \space))
  ([n precision decimal-sep] (num->str n precision decimal-sep \space))
  ([n precision decimal-sep thousands-sep]
   (format
    (str "%." precision \f)
    (bigdec n)))) ; locale is read from a host for now


                                        ; booleans

(boolean "12.34") ; see truthiness of a value
(boolean nil)
(boolean false)

  
                                        ; dates


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
