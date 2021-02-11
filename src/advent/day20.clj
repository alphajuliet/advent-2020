(ns advent.day20
  (:require [advent.util :as util]
            [clojure.pprint :as pp]
            [clojure.string :as str]))

;;----------------
(defn bin->dec [b] (Integer/parseInt b 2))

(defn bin-rev
  "Reverse the number in 10-bit binary space."
  [n]
  (->> n
       (pp/cl-format nil "~10,'0b")
       str/reverse
       bin->dec))


(defn tile-edges
  [t]
  (let [number (Integer/parseInt (re-find #"\d+" (first t)))
        e-top (bin->dec (second t))
        e-right (->> t rest util/T last reverse (str/join "") bin->dec)
        e-bottom (bin->dec (last t))
        e-left (->> t rest util/T first reverse (str/join "") bin->dec)
        edges [e-top e-right e-bottom e-left]]
    {:tile number
     :edges (into edges (map bin-rev edges))}))

(defn tupleize
  "Turn into a collection of tuples with the key and values."
  [m]
  (into [] (map #(vector (:tile m) %) (:edges m))))

(defn read-tiles
  [f]
  (->> f
       util/import-data
       (partition-by (comp zero? count))
       (filter (comp #(< 1 %) count))
       (util/mapmap #(str/replace % "." "0"))
       (util/mapmap #(str/replace % "#" "1"))))

(defn part1
  "Functional pipeline to manipulate the tiles into an answer."
  [f]
  (->> f
       read-tiles
       (map (comp tupleize tile-edges))
       (apply concat)
       (sort-by second)
       (partition-by second)
       (filter #(> (count %) 1))
       (apply concat)
       (group-by first)
       (filter #(= (count (val %)) 4))
       keys
       (apply *)))

(def testf "data/day20-test.txt")
(def inputf "data/day20-input.txt")

(def z (read-tiles testf))

;; The End
