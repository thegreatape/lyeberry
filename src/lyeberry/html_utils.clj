(ns lyeberry.html-utils
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]))

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

(defn url-encode-book
  [book]
  (-> (str (:title book) " " (:author book))
      (clojure.string/replace "%" "")
      clojure.string/lower-case
      client/url-encode-illegal-characters))

(defn select-from-html-string
  [page-html selector-list]
  (-> page-html
      (java.io.StringReader.)
      (html/html-resource)
      (html/select #{selector-list})) )
