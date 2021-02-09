(ns advent.day19
  (:require [advent.util :as util]
            [clojure.string :as str]
            [instaparse.core :as insta]))

;;----------------
(defn read-rules
  [f]
  (->> f
       util/import-data))

(defn get-parser
  [rules]
  (->> rules
       sort
       (str/join "; ")
       insta/parser))
;;----------------
(defn go
  [f]
  (let [in (read-rules f)
        [rules messages] (split-with (comp pos? count) in)
        parser (get-parser rules)]
    (->> messages
         rest
         (map #(insta/parses parser %))
         (util/count-if (comp pos? count)))))

(def testf "data/day19-test.txt")
(def testf2 "data/day19-test2.txt")
(def inputf "data/day19-input.txt")
(def inputf2 "data/day19-input2.txt")

;; The End
