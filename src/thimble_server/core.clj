(ns thimble-server.core
  (:use     [compojure.core])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as ring-json]
            [thimble-server.route-handlers.user :as user]
            [ring.util.response :refer [response]]
            [thimble-server.data-access-layer.post :as post-data]))

(defroutes main-routes
  (POST "/api/user" [username password] (user/handle-create-user username password))
  (GET "/api/user/:username" [username] (user/handle-get-user-info username))

  (POST "/api/post" [username replyto] (let [new-id (post-data/create-post! username
                                                                            (nil? replyto))]
                                            (if (nil? new-id) {:status 400} (response {:postid new-id}))))

  (GET "/api/post/:username" [username] (post-data/get-original-posts-by-username username)))

  ;;(POST "/api/post/audio/:postid" [audio, postid] (post-data/add-file-to-post! ...))

(def app (->  main-routes
              ring-json/wrap-json-params
              ring-json/wrap-json-response))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
