(ns thimble-server.data-access-layer.post-test
  (:require [clojure.test :refer :all]
            [thimble-server.data-access-layer.post :refer :all]
            [thimble-server.data-access-layer.user :refer [user-exists?]]))

;; If the user exists and the query works the new post id is returned
(deftest can-create-post
  (with-redefs [get-next-postid  (fn [& _] 666)
                user-exists?     (fn [& _] true)
                insert-post!     (fn [& _] 1)]
    (is (= (create-post! "user1" true) 666))))

(deftest cannot-create-post-if-user-not-exists
  (with-redefs [get-next-postid  (fn [& _] 666)
                user-exists?     (fn [& _] false)
                insert-post!     (fn [& _] 1)]
    (is (= (create-post! "user1" true) nil))))

(deftest cannot-create-post-if-query-fails
  (with-redefs [get-next-postid  (fn [& _] 666)
                user-exists?     (fn [& _] true)
                insert-post!     (fn [& _] 0)]
    (is (= (create-post! "user1" true) nil))))

;; if post exists and there is audio, can add to table
(deftest can-add-audio
  (with-redefs [post-exists?       (fn [& _] true)
                store-audio!       (fn [& _] "filename")
                update-post-audio! (fn [& _] 1)]
    (is (= (add-file-to-post! "someaudio" 2) 1))))

;; if post exists and there is no audio, cannot add to table
(deftest cannot-add-audio-because-no-audio-supplied
  (with-redefs [post-exists?       (fn [& _] true)
                store-audio!       (fn [& _] "filename")
                update-post-audio! (fn [& _] 1)]
    (is (= (add-file-to-post! nil 2) nil))))

;; if post does not exists and there is audio, cannot add to table
(deftest cannot-add-audio-because-post-DNE
  (with-redefs [post-exists?       (fn [& _] false)
                store-audio!       (fn [& _] "filename")
                update-post-audio! (fn [& _] 1)]
    (is (= (add-file-to-post! "someaudio" 2) nil))))

;; if post does not exists and there is no audio, cannot add to table
(deftest cannot-add-audio-because-post-DNE-and-no-audio-supplied
  (with-redefs [post-exists?       (fn [& _] false)
                store-audio!       (fn [& _] "filename")
                update-post-audio! (fn [& _] 1)]
    (is (= (add-file-to-post! "someaudio" 2) nil))))

;; QUESTION/TODO: how do we test adding a filename that already exists?
