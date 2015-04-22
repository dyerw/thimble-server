(ns thimble-server.core
  (:use     [compojure.core])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as ring-json]
            [thimble-server.data-access-layer.user :as user-data]))

(defroutes main-routes
  (POST "/api/user" {username :username
                     password :password}
                     (user-data/create-new-user! username password))
  (GET "/api/user/:username" [username]
                   (user-data/get-password-hash username)))

(def app (->  main-routes
              ring-json/wrap-json-params))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
