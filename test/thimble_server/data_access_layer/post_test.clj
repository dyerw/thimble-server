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

;; if post exists and there is audio, can update post
(deftest can-add-audio
  (with-redefs [post-exists?               (fn [& _] true)
                store-audio!               (fn [& _] "filename")
                post-has-audio?            (fn [& _] false)
                audio-hash-already-exists? (fn [& _] false)
                update-post-audio!         (fn [& _] 1)]
    (is (add-file-to-post! "someaudio" 2))))

;; if post exists and there is no audio, cannot update post
(deftest cannot-add-audio-because-no-audio-supplied
  (with-redefs [post-exists?               (fn [& _] true)
                store-audio!               (fn [& _] "filename")
                post-has-audio?            (fn [& _] false)
                audio-hash-already-exists? (fn [& _] false)
                update-post-audio!         (fn [& _] 1)]
    (is (not (add-file-to-post! nil 2)))))

;; if post does not exists and there is audio, cannot update post
(deftest cannot-add-audio-because-post-DNE
  (with-redefs [post-exists?               (fn [& _] false)
                store-audio!               (fn [& _] "filename")
                post-has-audio?            (fn [& _] false)
                audio-hash-already-exists? (fn [& _] false)
                update-post-audio!         (fn [& _] 1)]
    (is (not (add-file-to-post! "someaudio" 2)))))

;; if post already has audio, cannot update post
(deftest cannot-add-audio-because-post-already-has-audio
  (with-redefs [post-exists?               (fn [& _] true)
                store-audio!               (fn [& _] "filename")
                post-has-audio?            (fn [& _] true)
                audio-hash-already-exists? (fn [& _] false)
                update-post-audio!         (fn [& _] 1)]
    (is (not (add-file-to-post! "someaudio" 2)))))

;; if query fails, cannot update post
(deftest cannot-add-audio-because-query-failed
  (with-redefs [post-exists?               (fn [& _] true)
                store-audio!               (fn [& _] "filename")
                post-has-audio?            (fn [& _] false)
                audio-hash-already-exists? (fn [& _] false)
                update-post-audio!         (fn [& _] 0)]
    (is (not (add-file-to-post! "someaudio" 2)))))

;; if hash from store audio is already used, cannot update post
(deftest cannot-add-audio-because-hash-already-exists
  (with-redefs [post-exists?               (fn [& _] true)
                store-audio!               (fn [& _] "filename")
                post-has-audio?            (fn [& _] false)
                audio-hash-already-exists? (fn [& _] true)
                update-post-audio!         (fn [& _] 0)]
    (is (not (add-file-to-post! "someaudio" 2)))))
