;; day02.clj

(ns advent.day02
  (:require [clojure.string :as str]
            [advent.util :as util]))

;; ----------------
;; Utilities

(def testf "data/day02-test.txt")
(def inputf "data/day02-input.txt")

(defn- get-range
  "Turn a string range into a vector, e.g \"1-3\" => (1 3)"
  [s]
  (map #(Integer/parseInt %) (str/split s #"\-")))

(defn eor
  "Logical exclusive or"
  [p q]
  (or (and p (not q))
      (and q (not p))))

;; ----------------
;; Part 1

(defn matcher1
  [line]
  (let [[range x s] (str/split line #"\s+")
        [min max] (get-range range)
        ch (subs x 0 1)
        n (count (re-seq (re-pattern ch) s))]
    (<= min n max)))

;; ----------------
;; Part 2

(defn matcher2
  [line]
  (let [[x y s] (str/split line #":?\s+")
        [p1 p2] (get-range x)]
    (eor (= y (subs s (dec p1) p1))
         (= y (subs s (dec p2) p2)))))

;; ----------------
(defn count-valid
  [policy f]
  (->> f
       (util/import-data)
       (map policy)
       (filter true?)
       count))

(defn part1
  [f]
  (count-valid matcher1 f))

(defn part2
  [f]
  (count-valid matcher2 f))
