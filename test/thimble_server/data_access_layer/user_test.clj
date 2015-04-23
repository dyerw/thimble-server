(ns thimble-server.data-access-layer.user-test
  (:require [clojure.test :refer :all]
            [thimble-server.data-access-layer.user :refer :all]))

;; If the user does not exist and the insert query works
;; we should return true
(deftest can-create-user
  (with-redefs [get-user     (fn [& _] {})
                insert-user! (fn [& _] 1)]
    (is (create-user! "user1" "pass1"))))

;; If the user exists we return false
(deftest cannot-create-existing-user
  (with-redefs [get-user     (fn [& _] {:username "user1"
                                        :password "pass1"})
                insert-user! (fn [& _] 1)]
    (is (not (create-user! "user1" "pass1")))))

;; If the SQL Query fails we return false
(deftest cannot-create-user-with-failing-query
  (with-redefs [get-user     (fn [& _] {})
                insert-user! (fn [& _] 0)]
    (is (not (create-user! "user1" "pass1")))))
