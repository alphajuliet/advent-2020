;; day03.clj

(ns advent.day03
  (:require [clojure.string :as str]
            [advent.util :as util]))

;; ----------------

(def f0 "data/day03-test.txt")
(def f1 "data/day03-input.txt")


(def a0 (util/import-data f0))
(def a1 (util/import-data f1))

(defn is-tree?
  [rots s]
  (let [s1 (util/rotate-string rots s)]
    (= "#" (subs s1 0 1))))

(defn check-trees
  [x y coll]
  (for [i (range 0 (count coll) y)
        :let [j (/ (* i x) y)]]
    (is-tree? j (nth coll i))))

(defn count-trees
  [x y coll]
  (->> coll
       (check-trees x y)
       (filter true?)
       (count)))

(defn part1
  []
  (count-trees 3 1 a1))

(defn part2
  []
  (let [steps [[1 1] [3 1] [5 1] [7 1] [1 2]]]
    (->> steps
         (map #(count-trees (first %) (second %) a1))
         (apply *))))


;; The End
