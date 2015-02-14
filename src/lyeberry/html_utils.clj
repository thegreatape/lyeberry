(ns lyeberry.html-utils
  (:require [net.cgrand.enlive-html :as html]))

(defn text-at
  [node selector]
  (clojure.string/trim
       (first (html/texts
                (html/select node #{selector})))))

(defn text-of
  [node]
  (clojure.string/replace
    (clojure.string/trim (html/text node))
    #"\s+"
    " "))
