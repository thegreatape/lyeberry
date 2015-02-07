(defproject lyeberry "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [enlive "1.1.5"]
                 [clj-http "1.0.1"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler lyeberry.core/handler}
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}})

