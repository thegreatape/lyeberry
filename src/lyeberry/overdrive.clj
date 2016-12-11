(ns lyeberry.overdrive
  (:use lyeberry.html-utils)
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(def bpl-host
  "https://bpl.overdrive.com")

(def minuteman-host
  "https://minuteman.overdrive.com")

(defn search-url
  [root]
  (str root "/search"))

(defn search-params
  [book]
  {:query (-> (str (:title book) " " (:author book))
              (clojure.string/replace "%" "")
              (clojure.string/replace "?" ""))})

(defn search-results
  [book host]
  (let [response (client/get
                   (search-url host)
                   {:query-params (search-params book)})]
    (:body response)))

(defn media-item-json
  [response]
  (-> (re-find #"window.OverDrive.mediaItems =(.*);\s*\n" response)
      last
      (cheshire/parse-string true)))

(defn extract-copy
  [host [id copy-json]]
  {:title (:title copy-json)
   :author (:firstCreatorName copy-json)
   :url (str host "/media/" (name id))
   :status (str "Available Copies: "
                (:availableCopies copy-json)
                " of "
                (:ownedCopies copy-json)
                " ("
                (:holdsCount copy-json)
                " holds)")
   :location ""
   :call_number ""
   })

(defn extract-books
  [book-json host]
  (map (partial extract-copy host) book-json))

(defn copies
  [host book]
  (-> book
      (search-results host)
      media-item-json
      (extract-books host)))

(defn minuteman-copies
  [book]
  (copies minuteman-host book))

(defn bpl-copies
  [book]
  (copies bpl-host book))
