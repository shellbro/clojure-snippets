(ns clojure-snippets.idioms
  (:gen-class))

(empty? nil) ; true
(empty? []) ; true

;; not-empty?
(seq nil) ; nil
(seq []) ; nil
(seq [:a]) ; (:a)
