(ns lyeberry.core-test
  (:require [ring.mock.request :as mock])
  (:use [midje.sweet])
  (:use [lyeberry.core])
  (:use [ring.middleware.params :only [params-request]]))

(def happy-response
  {:status 200
   :headers {}
   :body "Yay!"})

(def test-authorized-token
  "12345")

(defn happy-handler
  [request]
  happy-response)

(facts "about wrap-token-auth"
  (fact "it calls the handler if the token matches"
    ( (wrap-token-auth happy-handler test-authorized-token)
     (params-request (mock/request :get "/" {:token test-authorized-token}))) => happy-response)

  (fact "it returns a 401 if the token does not match"
    ( (wrap-token-auth happy-handler test-authorized-token)
     (params-request (mock/request :get "/" {:token "this is wrong"}))) => (contains {:status 401})))
