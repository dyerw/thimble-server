(ns thimble-server.route-handlers.middleware)

(defn wrap-audio-file
  "Ring middleware that wraps a handler in functionality to move multipart
  form parameters into the :params map of the request."
  [handler]
  (let [keywordize (fn [request] (zipmap (map keyword (keys request))
                                         (vals request)))]
  (fn [request]
    (handler (assoc request
                    :params
                    (assoc (:params request)
                           :multipart-params
                           (keywordize
                             (:multipart-params request))))))))
