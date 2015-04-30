(ns thimble-server.route-handlers.middleware-test
  (:require [clojure.test :refer :all]
            [thimble-server.route-handlers.middleware :refer :all]))

;; File upload middleware should successfully
(deftest test-wrap-audio-file
  (let [fake-handler (fn [request] request)]
  (is (= {:multipart-params {"audio" {:info 123} "replyto" 666}
          :params {:multipart-params {:audio   {:info 123}
                                      :replyto 666}
                   :otherparams "are still here"}}
          ((wrap-audio-file fake-handler) {:params {:otherparams "are still here"}
                                           :multipart-params {"audio"   {:info 123}
                                                              "replyto" 666}})))))
