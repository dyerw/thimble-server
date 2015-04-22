(ns thimble-server.data-access-layer.config
  (:require [yesql.core :refer [defquery]]))

;; Database Connection Setup
(def db-spec {:classname    "org.sqlite.JDBC",
          	  :subprotocol  "sqlite",
        	    :subname	    "data/thimble.db"})

;; Create functions from our table create SQL queries
(def sql-dir "thimble_server/data_access_layer/sql/")
;; TODO: User sql-dir, figure out why it's not working
(defquery create-posts! "thimble_server/data_access_layer/sql/create/create_posts.sql")
(defquery create-users! "thimble_server/data_access_layer/sql/create/create_users.sql")

;; Create tables if they don't exist
(create-posts! db-spec)
(create-users! db-spec)
