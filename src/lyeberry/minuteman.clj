(ns lyeberry.minuteman
  (:use lyeberry.html-utils)
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]))

(defn is-header?
  [row]
  (not-empty (html/select row #{[:th]})))

(defn copies-from-book-node
  [book-node]
  (let [rows (html/select book-node #{ [:.itemTable :tr] })]
    (for [row rows
          :let [cells (html/select row #{ [:td] })]
          :when (not (is-header? row)) ]
      {:location    (clojure.string/capitalize
                      (clojure.string/replace (text-of (nth cells 0)) #"/.*" ""))
       :call_number (text-of (nth cells 1))
       :status      (text-of (nth cells 2))
       :url         "" })))

(defn extract-copies
  [book-node]
  (let [title (text-at book-node [:.dpBibTitle :span.title :a])
        author (clojure.string/replace
                 (text-at book-node [:.dpBibTitle :span.title :.customSecondaryText])
                 "/ " "")
        copies (copies-from-book-node book-node)]
   (for [copy copies] (merge {:title title :author author} copy))))

(defn extract-books
  [page-html]
  (flatten
    (map extract-copies
         (select-from-html-string page-html [:.searchResult]))))

(defn search-url
  [book]
   (str
     "https://find.minlib.net/iii/encore/search/C__S"
     (url-encode-book book)
     "__Orightresult__U?lang=eng&suite=cobalt"))

(defn copies
  [book]
  (extract-books (:body (client/get (search-url book)))))
