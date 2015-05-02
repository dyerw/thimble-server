(ns thimble-server.route-handlers.post
  (:require [ring.util.response :refer [response]]
            [gate :refer [defhandler]]
            [thimble-server.jobs :as jobs]
            [thimble-server.data-access-layer.post :as post-data]
            [thimble-server.route-handlers.middleware :refer [wrap-audio-file]]
            [thimble-server.route-handlers.authentication :refer [requires-authentication]]))

(defhandler handle-create-post
  "Creates a new post. Adding new data to the database and scheduling a job
   to upload the new file to S3.
   @param authuser [string] the user parsed from the authentication token
   @param multipart-params [hash-map] contains a userid if this is a reply
                                      and a reference to the file object
   @return status code depending on success/failure"
  [authuser multipart-params]
  (if-let [new-postid  (post-data/create-post! authuser
                                               (nil? (:replyto multipart-params)))]
    (do (jobs/store-file! new-postid (:tempfile (:audio multipart-params)))
        {:status 200})
    {:status 400}))

;; TODO: this route handler should handle getting original posts and all posts
(defhandler handle-get-posts-for-user [username] (post-data/get-original-posts-by-username username))

;; Route Mapping
(def routes {:path "/post/:username"
             :post (-> handle-create-post
                       wrap-audio-file
                       requires-authentication)
             :get  handle-get-posts-for-user})
