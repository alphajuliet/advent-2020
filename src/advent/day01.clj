;; day01

(ns advent.day01
  (:require [clojure.string :as str]))

;; Bring in numbers
(defn import-data
  [f]
  (->> f
       (slurp)
       (str/split-lines)
       (map #(Integer/parseInt %))
       (sort <)))

(def inputf "data/day01-input.txt")

;; Part 1
(defn part1
  [f]
  (let [coll (import-data f)]
    (for [i (filter (partial < 1010) coll)
          j (filter (partial >= 1010) coll)
          :when (= (+ i j) 2020)]
      (* i j))))

;; Part 2
(defn part2
  [f]
  (let [coll (import-data f)]
    (for [i coll
          j coll
          k coll
          :when (< i j k)
          :when (= (+ i j k) 2020)]
      (* i j k))))

;; The End
