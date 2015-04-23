(ns thimble-server.data-access-layer.post
  (:require [thimble-server.data-access-layer.config :refer [db-spec sql-dir]]
            [thimble-server.data-access-layer.user :refer :all]
            [thimble-server.data-access-layer.s3.store]
            [yesql.core :refer [defquery]]))

;; Create user functions from SQL queries
(defquery insert-post! "thimble_server/data_access_layer/sql/insert/insert_post.sql")
;; QUESTION: Do we even need this?
(defquery select-post "thimble_server/data_access_layer/sql/select/select_post.sql")
(defquery select-max-postid "thimble_server/data_access_layer/sql/select/select_max_postid.sql")
(defquery update-post-audio! "thimble_server/data_access_layer/sql/update/update_post_audio.sql")

(defn get-post
  "Retrieves user info for a username.
   Returns a map with :postid :audio-key :user"
  [postid]
  (select-post db-spec postid))

(defn get-next-postid
  "Retrieves the next unique postid, postids increment."
  []
  (+ 1 (:id (select-max-postid))))

(defn post-exists?
  "Returns true if post already exists in database.
  @param postid [integer] id of the post in question"
  [postid]
  (empty? (get-post postid)))

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

(defn add-file-to-post!
  "takes audio and post id
   calls file handler
   updates table so post row has filekey in entry
   @param   audio  [blob]    the audio file
   @param   postid [integer] post id the audio is for
   @returns        [integer] 1 or 0 success or failure"
   ; QUESTION: what should this return?
   ; what oes checking if audio is null look like? should we check here?
   [audio postid]
   (if (and (post-exists? postid) (not (empty? audio))
            (= (update-post-audio! (store-audio! audio) postid) 1))
       1 nil))
