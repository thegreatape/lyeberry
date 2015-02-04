(ns lyeberry.minuteman
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]))

(defn text-at
  [node selector]
  (clojure.string/trim
       (first (html/texts
                (html/select node #{selector})))))

(defn is-header?
  [row]
  (not-empty (html/select row #{[:th]})))

(defn text-of
  [node]
  (clojure.string/trim (html/text node)))

(defn copies-from-book-node
  [book-node]
  (let [rows (html/select book-node #{ [:.itemTable :tr] })]
    (for [row rows
          :let [cells (html/select row #{ [:td] })]
          :when (not (is-header? row)) ]
      {:location    (clojure.string/capitalize
                      (clojure.string/replace (text-of (nth cells 0)) #"/.*" ""))
       :call-number (text-of (nth cells 1))
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
    (map extract-copies (-> page-html
                            (java.io.StringReader.)
                            (html/html-resource)
                            (html/select #{[:.searchResult]})))))

(defn search-url
  [book]
   (str
     "https://find.minlib.net/iii/encore/search/C__S"
     (-> (str (:title book) " " (:author book))
         clojure.string/lower-case
         client/url-encode-illegal-characters)
     "__Orightresult__U?lang=eng&suite=cobalt"))

(defn copies
  [book]
  (extract-books (:body (client/get (search-url book)))))
