(ns thimble-server.data-access-layer.post
  (:require [thimble-server.data-access-layer.config :refer [db-spec sql-dir]]
            [thimble-server.data-access-layer.user :refer :all]
            [yesql.core :refer [defquery]]))

;; Create user functions from SQL queries
(defquery insert-post! "thimble_server/data_access_layer/sql/insert/insert_post.sql")
;; QUESTION: Do we even need this?
;;(defquery select-post "thimble_server/data_access_layer/sql/select/select_post.sql")
(defquery select-max-postid "thimble_server/data_access_layer/sql/select/select_max_postid.sql")

(defn get-next-postid
  "Retrieves the next unique postid, postids increment."
  []
  (+ 1 (:id (select-max-postid))))

(defn create-post!
  "Creates a new post in the database.
   @param poster      [string]  the username of the poster
   @param is-original [boolean] false if this is a reply to another post
   @returns           [integer] the new postid for this post"
  [poster is-original]
  (let [postid          (get-next-postid)
        is-original-int (if is-original 1 0)]
    (if (and (user-exists? poster)
             (= (insert-post! postid poster is-original-int) 1))
        postid nil)))
