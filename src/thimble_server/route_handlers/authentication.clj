(ns thimble-server.route-handlers.authentication
  (:require [buddy.auth :refer [authenticated?]]))

(defn requires-authentication
  "Ring middleware that wraps a handler in functionality to check if
   a request is authenticated and returns a 403 if it's not. Also adds
   the identity map from the request into the params map, so all fields
   in the JWS token are available as normal params."
  [handler]
  (fn [request]
    (if-not (authenticated? request) {:status 403}
      (handler (assoc request :params (merge (:params request)
                                             (:identity request)))))))
