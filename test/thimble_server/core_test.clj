(ns thimble-server.core-test
  (:require [clojure.test :refer :all]))

;; Common response map to check for
(def blank-response {:status  200
                     :headers {}
                     :body    nil})
