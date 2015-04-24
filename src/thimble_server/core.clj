(ns thimble-server.core
  (:use     [compojure.core])
            ;; Ring namespaces
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as ring-json]

            ;; Route handler namespaces
            [thimble-server.route-handlers.user :as user]
            [thimble-server.route-handlers.post :as post]))

(defroutes main-routes
  (POST "/api/user" [username password] (user/handle-create-user username password))
  (GET "/api/user/:username" [username] (user/handle-get-user-info username))

  (POST "/api/post" [username replyto] (post/handle-create-post username replyto))
  (GET "/api/post/:username" [username] (post/handle-get-posts-for-user username)))

  ;;(POST "/api/post/audio/:postid" [audio, postid] (post/add-file-to-post! ...))

;; Automatically translate incoming jason to clojure data and
;; outgoing clojure data to json
(def app (->  main-routes
              ring-json/wrap-json-params
              ring-json/wrap-json-response))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
