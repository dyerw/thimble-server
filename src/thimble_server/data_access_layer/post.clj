(ns thimble-server.data-access-layer.post
  (:require [thimble-server.data-access-layer.config :refer [db-spec sql-dir]]
            [thimble-server.data-access-layer.user :refer :all]
            [thimble-server.data-access-layer.file_storage.store]
            [yesql.core :refer [defquery]]))

;; Create user functions from SQL queries
(defquery insert-post! "thimble_server/data_access_layer/sql/insert/insert_post.sql")
(defquery select-post-by-id "thimble_server/data_access_layer/sql/select/select_post_by_id.sql")
(defquery select-post-by-audio-hash "thimble_server/data_access_layer/sql/select/select_post_by_audio_hash.sql")
(defquery select-posts-by-username "thimble_server/data_access_layer/sql/select/select_posts_by_username.sql")
(defquery select-max-postid "thimble_server/data_access_layer/sql/select/select_max_postid.sql")
(defquery update-post-audio! "thimble_server/data_access_layer/sql/update/update_post_audio.sql")

(defn get-post-by-id
  "Retrieves post info for for a given post id.
   Returns a map with :id :file :poster"
  [postid]
  (select-post-by-id db-spec postid))

(defn get-original-posts-by-username
  "Retrieves a list of all original posts for a given username.
   @param username [string] the username to get the posts for
   @return [vector] a list containing "
  [username]
  (->> (select-posts-by-username db-spec username)
       (filter #(= 1 (:original %)))
       (map #(:id %))))

(defn get-post-by-audio-hash
  "Retrieves post info for for a given audio hash.
   Returns a map with :id :file :poster"
  [audio-hash]
  (select-post-by-id db-spec audio-hash))

(defn get-next-postid
  "Retrieves the next unique postid, postids increment."
  []
  (let [max (:max (first (select-max-postid db-spec)))]
    (if (nil? max) 0 (inc max))))

(defn post-exists?
  "Returns true if post already exists in database.
  @param postid [integer] id of the post in question"
  [postid]
  (empty? (get-post-by-id db-spec postid)))

(defn post-has-audio?
  "Returns false if file column of selected row is nil.
  @param postid [integer] id of the post in question"
  [postid]
  (not (= (:file (get-post-by-id db-spec postid)) nil)))

(defn audio-hash-already-exists?
  "returns false if there is no post with supplied audio hash
  @param audio-hash [string] audio hash corresponding to audio in storage"
  [audio-hash]
  (empty? (get-post-by-audio-hash db-spec audio-hash)))

(defn create-post!
  "Creates a new post in the database.
   @param poster      [string]  the username of the poster
   @param is-original [boolean] false if this is a reply to another post
   @returns           [integer] the new postid for this post"
  [poster is-original]
  (let [postid          (get-next-postid)
        is-original-int (if is-original 1 0)]
    (if (and (user-exists? poster)
             (= (insert-post! db-spec postid poster is-original-int) 1))
        postid nil)))

(defn add-file-to-post!
  "takes audio and post id
   calls file handler
   updates table so post row has filekey in entry
   @param   audio  [?]    the audio file
   @param   postid [integer] post id the audio is for
   @returns        [boolean]  success or failure"
   ; QUESTION: what is audio's type
   ; what oes checking if audio is null look like? should we check here?
   [audio postid]
   (let [audio-hash (store-audio! audio)]
     (and (post-exists? postid)
            (not (empty? audio))
            (not (post-has-audio? postid))
            (not (audio-hash-already-exists? audio-hash))
            (= (update-post-audio! db-spec audio-hash postid) 1))
       ))
