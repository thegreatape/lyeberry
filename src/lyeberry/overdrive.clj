(ns lyeberry.overdrive
  (:use lyeberry.html-utils)
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]))

(def bpl-host
  "http://overdrive.bpl.org")

(def minuteman-host
  "http://digital.minlib.net")

(defn search-url
  [root]
  (str root "BANGSearch.dll?Type=FullText&PerPage=24&URL=SearchResultsList.htm"))

(defn site-root
  [host]
  "Get the site's base for a new session; Overdrive sites appear to place this
  in the url and require it."
  (let [url (last (:trace-redirects (client/get host)))]
    (clojure.string/replace url #"[^/]+$" "")))


(defn search-params
  [book]
  {:Sort "SortBy=Relevancy"
   :FullTextField "All"
   :FullTextCriteria (str (:title book) " " (:author book))})

(defn search-results
  [book host]
  (let [search-post (client/post
                      (search-url (site-root host))
                      {:form-params (search-params book)})]
   (:body
    (client/get
      (str host (get (:headers search-post) "Location"))))))

(defn extract-copy
  [result-node]
  {:title (text-at result-node [:.trunc-title-line-list])
   :author (text-at result-node [:.trunc-author-line-list])
   :url (:href (:attrs (first (html/select result-node [:.trunc-title-line-list :a]))))
   :status (text-at result-node [:.trunc-avail-copies-list]) })

(defn annotate-copy
  [root copy]
  (merge copy {:location "" :call_number "" :url (str root (:url copy))} ))

(defn extract-books
  [page-html]
  (map extract-copy
       (-> page-html
           (java.io.StringReader.)
           (html/html-resource)
           (html/select #{[:.searchResultRow]}))))

(defn copies
  [host book]
  (map
    #(annotate-copy (site-root host) %1)
    (extract-books (search-results book host))))

(defn minuteman-copies
  [book]
  (copies minuteman-host book))

(defn bpl-copies
  [book]
  (copies bpl-host book))
