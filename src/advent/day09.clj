;; day09.clj

(ns advent.day09
  (:require [advent.util :as util]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

;;----------------
(defn read-numbers
  [f]
  (->> f
      util/import-data
      (map #(Integer/parseInt %))))

(defn sums
  "Sum of every combination of 2 numbers from the given list."
  [v]
  (map #(apply + %)
       (combo/combinations v 2)))

(defn not-a-sum
  "Find the first number that is not a sum of two of the numbers in the window preceding it."
  [window coll]
  (let [p (partition (inc window) 1 coll)]
    (as-> p <>
      (map #(some #{(last %)} (sums (take window %))) <>)
      (.indexOf <> nil)
      (nth p <>)
      (last <>))))

;;----------------
(def testf "data/day09-test.txt")
(def inputf "data/day09-input.txt")

(defn part1
  [window f]
  (not-a-sum window (read-numbers f)))

;; The End
