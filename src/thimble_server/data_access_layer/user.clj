(ns thimble-server.data-access-layer.user
  (:require [thimble-server.data-access-layer.config :refer [db-spec sql-dir]]
            [yesql.core :refer [defquery]]))

;; Create user functions from SQL queries
(defquery insert-user! "thimble_server/data_access_layer/sql/insert/insert_user.sql")
(defquery select-user "thimble_server/data_access_layer/sql/select/select_user.sql")

(defn create-new-user!
  "Creates a new user and adds it to the database."
  [username password]
  (insert-user! db-spec username password))

(defn get-password-hash
  "Retrieves password hash for a username."
  [username]
  (:password (select-user db-spec username)))
