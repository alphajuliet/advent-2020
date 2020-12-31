;; day09.clj

(ns advent.day09
  (:require [advent.util :as util]
            [clojure.math.combinatorics :as combo]))

;;----------------
(defn read-numbers
  [f]
  (->> f
      util/import-data
      (map #(Integer/parseInt %))))

;;----------------
;; Part 1

(defn sums
  "Sum of every combination of 2 numbers from the given list."
  [v]
  (map #(apply + %)
       (combo/combinations v 2)))

(defn not-a-sum
  "Find the first number that is not a sum of two of the numbers
  in the window preceding it."
  [window coll]
  (let [p (partition (inc window) 1 coll)]
    (as-> p <>
      (map #(some #{(last %)} (sums (take window %))) <>)
      (.indexOf <> nil)
      (nth p <>)
      (last <>))))

;;----------------
;; Part 2

(defn generate-seqs
  "Generate all the consecutive sequences in the collection."
  [coll]
  (let [max-i (dec (count coll))]
    (for [start (range max-i)
          len (range 2 (- max-i start))]
      (take len (drop start coll)))))

(defn check-sums
  [n coll]
  (let [subseq (take-while #(not= n %) coll)
        s (generate-seqs subseq)]
    (->> s
         (filter #(= n (apply + %)))
         first
         (apply (juxt min max))
         (apply +))))

;;----------------
(def testf "data/day09-test.txt")
(def inputf "data/day09-input.txt")

(defn part1
  [window f]
  (not-a-sum window (read-numbers f)))

(defn part2
  [window f]
  (let [x (part1 window f)]
    (check-sums x (read-numbers f))))

;; The End
