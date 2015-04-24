(ns thimble-server.route-handlers.post
  (:require [ring.util.response :refer [response]]
            [thimble-server.data-access-layer.post :as post-data]))

(defn handle-create-post
  "Creates a new post.
  @param username [string] the user creating this post
  @param replyto  [int]    the post id if this is a reply, otherwise nil
  @returns 400 error if we can't create the post or map containing the id
           assigned to this post"
  [username replyto]
  (let [new-id (post-data/create-post! username (nil? replyto))]
    (if (nil? new-id) {:status 400}
                      (response {:postid new-id}))))

;; TODO: this route handler should handle getting original posts and all posts
(defn handle-get-posts-for-user [username] (post-data/get-original-posts-by-username username))
