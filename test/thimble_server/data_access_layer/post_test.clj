(ns thimble-server.data-access-layer.post-test
  (:require [clojure.test :refer :all]
            [thimble-server.data-access-layer.post :refer :all]
            [thimble-server.data-access-layer.user :refer [user-exists?]]))

;; If the user exists and the query works the new post id is returned
(deftest can-create-post
  (with-redefs [get-next-postid (fn [& _] 666)
                user-exists?     (fn [& _] true)
                insert-post!     (fn [& _] 1)]
    (is (= (create-post! "user1" true) 666))))

(deftest cannot-create-post-if-user-not-exists
  (with-redefs [get-next-postid (fn [& _] 666)
                user-exists?     (fn [& _] false)
                insert-post!     (fn [& _] 1)]
    (is (= (create-post! "user1" true) nil))))

(deftest cannot-create-post-if-query-fails
  (with-redefs [get-next-postid (fn [& _] 666)
                user-exists?     (fn [& _] true)
                insert-post!     (fn [& _] 0)]
    (is (= (create-post! "user1" true) nil))))
