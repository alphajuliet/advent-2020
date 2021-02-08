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
(defn part1
  [f]
  (let [in (read-rules f)
        [rules messages] (split-with (comp pos? count) in)
        parser (get-parser rules)]
    (->> messages
         rest
         (map #(insta/parses parser %))
         (util/count-if (comp pos? count)))))


(def testf "data/day19-test.txt")
(def inputf "data/day19-input.txt")

(def z (read-rules inputf))
(def y (split-with (comp pos? count) z))
(def p (get-parser (first y)))
(def m (rest (second y)))

;; The End
