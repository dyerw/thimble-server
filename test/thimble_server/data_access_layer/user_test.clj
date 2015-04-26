(ns thimble-server.data-access-layer.user-test
  (:require [clojure.test :refer :all]
            [thimble-server.data-access-layer.user :refer :all]))

;; If the user does not exist and the insert query works
;; we should return true
(deftest can-create-user
  (with-redefs [user-exists? (fn [& _] false)
                insert-user! (fn [& _] 1)]
    (is (create-user! "user1" "pass1"))))

;; If the user exists we return false
(deftest cannot-create-existing-user
  (with-redefs [user-exists? (fn [& _] true)
                insert-user! (fn [& _] 1)]
    (is (not (create-user! "user1" "pass1")))))

;; If the SQL Query fails we return false
(deftest cannot-create-user-with-failing-query
  (with-redefs [user-exists? (fn [& _] false)
                insert-user! (fn [& _] 0)]
    (is (not (create-user! "user1" "pass1")))))

(deftest test-user-exists?-with-no-user
  (with-redefs [get-user-info (fn [& _] nil)]
    (is (not (user-exists? "user1")))))

(deftest test-user-exists?-with-user
  (with-redefs [get-user-info (fn [& _] {:username "user" :password "pass"})]
    (is (user-exists? "user1"))))
