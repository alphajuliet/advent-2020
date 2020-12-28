;; day05.clj

(ns advent.day05
  (:require [advent.util :as util]
            [clojure.set :as set]))

;;----------------
(defn read-bin-data
  [f]
  (->> f
       util/import-data
       (map #(Integer/parseInt % 2))))

;;----------------
(def input "data/day05-input-bin.txt")

(defn part1
  "Find the largest seat ID."
  []
  (->> input
       read-bin-data
       (apply max)))

(defn part2
  []
  (let [z (read-bin-data input)
        zmin (apply min z)
        zmax (apply max z)
        r (range zmin (inc zmax))]
    (set/difference (set r) (set z))))

;; The End
