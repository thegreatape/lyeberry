(ns lyeberry.core
  (:use [ring.util.response]
        [ring.middleware.params :only [wrap-params]]
        [ring.middleware.json :only [wrap-json-response]])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [lyeberry.minuteman :as minuteman]))

(def fetchers
  { "minuteman" minuteman/copies })

(defroutes app-routes
  (GET "/systems/:id/books" [id title author]
       (let [fetcher (fetchers id)]
         (if fetcher
           (response (fetcher {:author author :title title}))
           {:body {:error "Library system not found"}
            :headers {}
            :status 404})))

  (route/not-found "Route not found"))

(def app
  (-> app-routes
      (wrap-params)
      (wrap-json-response)))

(defn start [port]
  (jetty/run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
