(ns thimble-server.core
            ;; Ring namespaces
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as ring-json]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.util.response :refer [response]]

            ;; Gate Routing Lib
            [gate :refer [defrouter handler defhandler]]

            ;; Auth namespaces
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.backends.token :refer [jws-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [thimble-server.route-handlers.authentication :refer [secret]]

            ;; Route handler namespaces
            [thimble-server.route-handlers.user :as user]
            [thimble-server.route-handlers.post :as post]))

;;; Router Setup
(defrouter approuter [{:path "/api"
                       :children [post/routes user/routes]}])

;; Create and wrap all wrappers that are on all routes
(def auth-backend (jws-backend {:secret secret
                               :options {:alg :hs512}}))

(def app (-> approuter
             (wrap-authentication auth-backend)
             wrap-multipart-params
             ring-json/wrap-json-params
             ring-json/wrap-json-response))

(defn -main []
  (jetty/run-jetty app {:port 3000}))
