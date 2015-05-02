(ns thimble-server.jobs
  (:require [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.basic     :as lb]
            [clojure.data.json :as json]
            [environ.core :refer [env]]))

(def rmq-conn (rmq/connect {:username (env :rmquser)
                            :password (env :rmqpass)}))
(def rmq-ch (lch/open rmq-conn))
(def qname "thimble.jobs.file-upload")
(def default-exchange "")
(lq/declare rmq-ch qname {:exclusive false})

(defn store-file!
  [postid file]
  (lb/publish rmq-ch default-exchange qname
    (json/write-str {:postid postid :file-path (.getPath file)})
    {:content-type "text/json"}))
