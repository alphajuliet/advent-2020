;; day10.clj

(ns advent.day10
  (:require [advent.util :as util]
            [clojure.math.combinatorics :as combo]))

;;----------------
(defn read-adaptors
  "Read in the adaptors, add 0 and the final rating and sort."
  [f]
  (as-> f <>
    (util/import-data <>)
    (map #(Integer/parseInt %) <>)
    (cons 0 <>)
    (cons (+ 3 (apply max <>)) <>)
    (sort < <>)))

(defn deltas
  "Return the deltas of consecutive elements."
  [coll]
  (map - (rest coll) coll))

(defn paths
  "Work out the number of paths given the number of 1s"
  [coll]
  (case coll
    [1 1 1 1 1] 49
    [1 1 1 1] 7
    [1 1 1] 4
    [1 1] 2
    1))

;;----------------
(def test1f "data/day10-test1.txt")
(def test2f "data/day10-test2.txt")
(def inputf "data/day10-input.txt")

(defn part1
  [f]
  (as-> f <>
    (read-adaptors <>)
    (deltas <>) ; Generate the consecutive differences
    (frequencies <>)     ; I learnt a new core function!
    (* (get <> 1) (get <> 3))))

(defn part2
  [f]
  (let [s (read-adaptors f)]
    (->> s
      deltas
      (partition-by #(= 3 %))
      (map paths)
      (apply *))))

;; The End
