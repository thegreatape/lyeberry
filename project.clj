(defproject lyeberry "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.6.3"]
                 [enlive "1.1.5"]
                 [clj-http "1.0.1"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-mock "0.2.0"]
                 [compojure "1.3.1"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [thegreatape/ring-raygun "0.1.0"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler lyeberry.core/app}
  :profiles {:dev     {:dependencies [[midje "1.5.1"]]}
             :uberjar {:aot :all}}
  :min-lein-version "2.5.0"
  :uberjar-name "lyeberry.jar")

