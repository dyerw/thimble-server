(ns thimble-server.core
  (:use     [compojure.core])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as ring-json]
            [thimble-server.data-access-layer.user :as user-data]))

(defroutes main-routes
  (POST "/api/user" [username password]
                     (if (user-data/create-user! username password)
                         ;; Return 200 if successful 400 if error
                         {:status 200} {:status 400}))
  (GET "/api/user/:username" [username]
                   (user-data/get-user username))

  ;;(POST "/api/post" [poster replyto] (post-data/create-post! poster replyto))
  ;;(POST "/api/post/audio/:postid" [audio] (post-data/add-file-to-post! ...))
  )

(def app (->  main-routes
              ring-json/wrap-json-params
              ring-json/wrap-json-response))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
