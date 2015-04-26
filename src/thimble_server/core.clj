(ns thimble-server.core
            ;; Ring namespaces
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as ring-json]
            [ring.util.response :refer [response]]

            ;; Compojure Routing Lib
            [compojure.core :refer [defroutes POST GET]]

            ;; Auth namespaces
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.backends.token :refer [jws-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]

            ;; Route handler namespaces
            [thimble-server.route-handlers.user :as user]
            [thimble-server.route-handlers.post :as post]))

;; TODO: Move this stuff to an auth namespace
(def secret "secret")

;;; ROUTES
(defroutes main-routes
  (POST "/api/user" [username password] (user/handle-create-user username password))
  (GET "/api/user/:username" [username] (user/handle-get-user-info username))

  (GET "/api/auth/:username/:password" [username password] (user/authenticate-user username password))

  ;; TODO: Factor out this auth flow into an auth macro
  (POST "/api/post" request (if-not (authenticated? request) {:status 403}
                              (let [username (get-in request [:identity :user])
                                    replyto   (:replyto request)]
                                (post/handle-create-post username replyto))))

  (GET "/api/post/:username" [username] (post/handle-get-posts-for-user username)))

  ;;(POST "/api/post/audio/:postid" [audio, postid] (post/add-file-to-post! ...))

;; Create an instance of auth backend.
(def auth-backend (jws-backend {:secret secret
                                :options {:alg :hs512}}))

;; Automatically translate incoming jason to clojure data and
;; outgoing clojure data to json
(def app (->  main-routes
              (wrap-authentication auth-backend)
              ring-json/wrap-json-params
              ring-json/wrap-json-response))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
