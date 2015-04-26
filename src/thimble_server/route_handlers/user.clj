(ns thimble-server.route-handlers.user
  (:require [ring.util.response :refer [response]]
            [buddy.hashers :as hashers]
            [buddy.sign.jws :as jws]
            [thimble-server.data-access-layer.user :as user-data]))

;;; Route handling functions

(defn handle-create-user
  "Handles creating a user, storing username and hashed password in database
   and formatting a response.
   @param username [string] the username for the to be created user
   @param password [string] the password for the to be created user
   @returns 400 status map if there's an error and 200 for success"
  [username password]
  (if (user-data/create-user! username (hashers/encrypt password))
      {:status 200} {:status 400}))

(defn handle-get-user-info
  "Handles retrieving information about a user and formatting it into a
   response.
   @param username [string] the user to get information about
   @return 404 error if user isn't found or user data"
   [username]
   (let [user (user-data/get-user-info username)]
     (if (nil? user) {:status 404} (response user))))

(defn authenticate-user
  "Handles user authentication,checks to make sure provided password matches
   the one in the data base.
   @returns signed token if successful, auth error if not"
   [username password]
   (if (hashers/check password (user-data/get-user-password username))
     ;; TODO: Get the real secret from config file
     (response {:token (jws/sign {:user username} "secret" {:alg :hs512})})
     {:status 401}))
