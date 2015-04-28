(ns thimble-server.route-handlers.post
  (:require [ring.util.response :refer [response]]
            [gate :refer [defhandler]]
            [thimble-server.data-access-layer.post :as post-data]
            [thimble-server.route-handlers.authentication :refer [requires-authentication]]))

(defhandler handle-create-post
  ;"Creates a new post.
  ;@param username [string] the user creating this post
  ;@param replyto  [int]    the post id if this is a reply, otherwise nil
  ;@returns 400 error if we can't create the post or map containing the id
  ;         assigned to this post"
  [username replyto]
  (let [new-id (post-data/create-post! username (nil? replyto))]
    (if (nil? new-id) {:status 400}
                      (response {:postid new-id}))))

;; TODO: this route handler should handle getting original posts and all posts
(defhandler handle-get-posts-for-user [username] (post-data/get-original-posts-by-username username))

;; Route Mapping
(def routes {:path "/post/:username"
             :post (requires-authentication handle-create-post)
             :get  handle-get-posts-for-user})
