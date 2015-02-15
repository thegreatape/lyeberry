(ns lyeberry.boston
  (:use lyeberry.html-utils)
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]))

(def site-root
  "http://bpl.bibliocommons.com")

(defn search-url
  [book]
  (str site-root
       "/search?q="
       (url-encode-book book)
       "&t=smart&search_category=keyword&commit=Search&submitsearch=go&se=catalog"))

(defn extract-copy
  [row]
  (let [cells (html/select row [:td])]
    { :location (text-of (first cells))
      :call_number (text-of (nth cells 2))
      :status (text-of (nth cells 3))
     }))

(defn reject-notes
  [rows]
  (remove (comp #(.contains % "note") :class :attrs) rows))

(defn extract-copies
  [availability-html]
  (let [rows (select-from-html-string availability-html [:#circulation_details :tbody :tr])
        title (first (select-from-html-string availability-html [:.bib_title :.title]))
        author (select-from-html-string availability-html [:.bib_title :.author])
        copy-title (apply str (text-of title) " " (map text-of author))]
    (map
      #(merge (extract-copy %) {:url "" :title copy-title})
      (reject-notes rows))) )

(defn book-section?
  [section]
  (= "Book"
     (text-at section [:.format :strong])))

(defn result-sections
  [page-html]
  (filter
    book-section?
    (select-from-html-string page-html [:.searchResults :.listItem] )))

(defn extract-copy-links
  [page-html]
  (map
    (comp #(str site-root %) :href :attrs)
    (html/select (result-sections page-html) [:.availability :a])))

(defn copies
  [book]
   (flatten
     (map (comp extract-copies :body client/get)
          (extract-copy-links (:body (client/get (search-url book)))))))
