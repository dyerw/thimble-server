(defproject thimble-server "0.1.0-SNAPSHOT"
  :description "API for Thimble Mobile App"

  :dependencies [[org.clojure/clojure "1.6.0"]

                 ;; Web Libs
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [gate "0.0.18"]

                 ;; SQL Libs
                 [yesql "0.4.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.xerial/sqlite-jdbc "3.8.7"]

                 ;; Auth Libs
                 [buddy/buddy-auth "0.5.0"]
                 [buddy/buddy-hashers "0.4.2"]]

            ;; Code Quality tools
  :plugins [[lein-cloverage "1.0.2"]
            [jonase/eastwood "0.2.1"]]

  :eastwood {:exclude-linters [:constant-test]}

  :main ^:skip-aot thimble-server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
