(ns thimble-server.route-handlers.user
  (:require [ring.util.response :refer [response]]
            [thimble-server.data-access-layer.user :as user-data]))

;;; Route handling functions

(defn handle-create-user
  "Handles creating a user and formatting a response.
  @param username [string] the username for the to be created user
  @param password [string] the password for the to be created user
  @returns 400 status map if there's an error and 200 for success"
  [username password]
  (if (user-data/create-user! username password)
      {:status 200} {:status 400}))

(defn handle-get-user-info
  "Handles retrieving information about a user and formatting it into a
   response.
   @param username [string] the user to get information about
   @return 404 error if user isn't found or user data"
   [username]
   (let [user (user-data/get-user username)]
     (if (nil? user) {:status 404} (response user))))
