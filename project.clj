(defproject thimble-server "0.1.0-SNAPSHOT"
  :description "API for Thimble Mobile App"

  :dependencies [[org.clojure/clojure "1.6.0"]

                 ;; Web Libs
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [compojure "1.3.3"]

                 ;; SQL Libs
                 [yesql "0.4.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.xerial/sqlite-jdbc "3.8.7"]]

  :plugins [[lein-cloverage "1.0.2"]]

  :main ^:skip-aot thimble-server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
