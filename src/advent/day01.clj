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

(def a (import-data "data/day01-input.txt"))

;; Part 1
(def x
 (for [i (filter (partial < 1010) a)
       j (filter (partial >= 1010) a)
       :when (= (+ i j) 2020)]
   (* i j)))

;; Part 2
(def y
 (for [i a
       j a
       k a
       :when (< i j k)
       :when (= (+ i j k) 2020)]
   (* i j k)))

(time
 (println x y))

;; The End
