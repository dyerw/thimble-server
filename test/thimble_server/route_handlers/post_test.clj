(ns thimble-server.route-handlers.post-test
  (:require [clojure.test :refer :all]
            [thimble-server.core-test :refer [blank-response]]
            [thimble-server.route-handlers.post :refer :all]
            [thimble-server.jobs :as jobs]
            [thimble-server.data-access-layer.post :as post-data]))

(def new-post-request {:params {:multipart-form {:audio   "this is a file"
                                                 :replyto nil}
                                :authuser "user1"}})

(deftest successful-post-creation
  (with-redefs [post-data/create-post! (fn [& _] 666)
                jobs/store-file! (fn [& _] nil)]
    (is (= {:status 200} (handle-create-post new-post-request)))))

(deftest failed-database-post-creation
  (with-redefs [post-data/create-post! (fn [& _] nil)
                jobs/store-file! (fn [& _] nil)]
    (is (= {:status 400} (handle-create-post new-post-request)))))
