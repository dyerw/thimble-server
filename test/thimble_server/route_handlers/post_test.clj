(ns thimble-server.route-handlers.post-test
  (:require [clojure.test :refer :all]
            [thimble-server.core-test :refer [blank-response]]
            [thimble-server.route-handlers.post :refer :all]
            [thimble-server.data-access-layer.post :as post-data]))

(def expected-response
  (merge blank-response {:body {:postid 666}}))

(deftest successful-post-creation
  (with-redefs [post-data/create-post! (fn [& _] 666)]
    (is (= expected-response (handle-create-post "user" 345)))))

(deftest failed-post-creation
  (with-redefs [post-data/create-post! (fn [& _] nil)]
    (is (= {:status 400} (handle-create-post "user" 345)))))
