(ns thimble-server.route-handlers.authentication-test
  (:require [clojure.test :refer :all]
            [thimble-server.route-handlers.authentication :refer [requires-authentication]]
            [buddy.auth :refer [authenticated?]]))

;; If not authenticated, returns 403
(deftest not-authenticated-denies-access
  (with-redefs [authenticated? (fn [& _] false)]
    (is (= {:status 403}
           ((requires-authentication (fn [req] {:status 200})) {})))))

;; If authenticated, returns 200
(deftest authenticated-allows-access
  (with-redefs [authenticated? (fn [& _] true)]
    (is (= {:status 200}
           ((requires-authentication (fn [req] {:status 200})) {})))))

;; Stuff passed in in identity gets merged into params
(deftest identity-goes-to-params
  (with-redefs [authenticated? (fn [& _] true)]
    (is (= {:a 1 :b 2 :c 3}
           ((requires-authentication (fn [req] (:params req))) {:params {:a 1 :b 2}
                                                                :identity {:c 3}})))))
