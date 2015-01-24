(ns lyeberry.core
  (:require [clj-http.client :as client]))

(defn minuteman-copies
  [book]
  [])

(defn minuteman-search-url
  [book]
   (str
     "https://find.minlib.net/iii/encore/search/C__S"
     (-> (str (:title book) " " (:author book))
         clojure.string/lower-case
         client/url-encode-illegal-characters)
     "__Orightresult__U?lang=eng&suite=cobalt"))
