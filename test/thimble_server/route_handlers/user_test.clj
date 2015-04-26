(ns thimble-server.route-handlers.user-test
  (:require [clojure.test :refer :all]
            [thimble-server.core-test :refer [blank-response]]
            [thimble-server.route-handlers.user :refer :all]
            [thimble-server.data-access-layer.user :as user-data]))

;; Tests the route handler for a successful user creation
(deftest create-user-success
  (with-redefs [user-data/create-user! (fn [& _] true)]
    (is (= (handle-create-user "user" "pass") {:status 200}))))

;; Tests the route handler for a failed user creation
(deftest create-user-failure
  (with-redefs [user-data/create-user! (fn [& _] false)]
    (is (= (handle-create-user "user" "pass") {:status 400}))))


;; Tests getting users that do and do not exist
(def expected-response
  (merge blank-response {:body {:username "user" :password "pass"}}))

(deftest get-existing-user
  (with-redefs [user-data/get-user-info (fn [& _] {:username "user"
                                              :password "pass"})]
    (is (= (handle-get-user-info "user") expected-response))))

(deftest get-nonexisting-user
  (with-redefs [user-data/get-user-info (fn [& _] nil)]
    (is (= (handle-get-user-info "user") {:status 404}))))
