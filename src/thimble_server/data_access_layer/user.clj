(ns thimble-server.data-access-layer.user
  (:require [thimble-server.data-access-layer.config :refer [db-spec sql-dir]]
            [yesql.core :refer [defquery]]))

;; Create user functions from SQL queries
(defquery insert-user! "thimble_server/data_access_layer/sql/insert/insert_user.sql")
(defquery select-user "thimble_server/data_access_layer/sql/select/select_user.sql")

(defn get-user
  "Retrieves user info for a username.
   Returns a map with :username :password :about"
  [username]
  (first (select-user db-spec username)))

(defn user-exists?
  "Returns true if user already exists in database."
  [username]
  (not (nil? (get-user username))))

(defn create-user!
  "Creates a new user and adds it to the database.
   Returns true if the operation was successful and false otherwise."
  [username password]
  (and (not (user-exists? username))
       (= (insert-user! db-spec username password) 1)))
