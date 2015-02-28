(ns lyeberry.core
  (:use [ring.util.response]
        [ring.middleware.params :only [wrap-params]]
        [ring.middleware.json :only [wrap-json-response]])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [lyeberry.minuteman :as minuteman]
            [lyeberry.overdrive :as overdrive]
            [lyeberry.boston :as boston]
            [ring.middleware.raygun :refer [wrap-raygun-handler]]))

(def fetchers
  { "minuteman" minuteman/copies
    "bpl-overdrive" overdrive/bpl-copies
    "minuteman-overdrive" overdrive/minuteman-copies
    "boston" boston/copies
    "explode" (fn [b] (throw (Exception. "oh no")))})

(defroutes app-routes
  (GET "/systems/:id/books" [id title author]
       (let [fetcher (fetchers id)]
         (if fetcher
           (response (fetcher {:author author :title title}))
           {:body {:error "Library system not found"}
            :headers {}
            :status 404})))

  (route/not-found "Route not found"))

(defn wrap-token-auth
  [handler authorized-token]
  (fn [request]
    (let [token (get-in request [:params "token"])]
      (if (= authorized-token token)
        (handler request)
        {:status 401 :body "Unauthorized"}))))

(def app
  (-> app-routes
      (wrap-raygun-handler (System/getenv "RAYGUN_APIKEY"))
      (wrap-json-response)
      (wrap-token-auth (System/getenv "AUTH_TOKEN"))
      (wrap-params)
      ))

(defn start [port]
  (jetty/run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
