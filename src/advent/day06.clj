;; day06.clj

(ns advent.day06
  (:require [clojure.set :as set]
            [clojure.string :as str]))

;;----------------

(defn swap [f x y] (f y x))


(defn read-answers
  "Read group answers."
  [f]
  (as-> f <>
    (slurp <>)
    (str/split <> #"\n\n")))

(def str->set (comp set (partial swap str/split #"")))
(def str-intersection (comp (partial apply set/intersection) (partial map str->set)))

(def testf "data/day06-test.txt")
(def inputf "data/day06-input.txt")

(defn part1
  [f]
  (->> f
       read-answers
       (map #(str/replace % #"\n|\s+" ""))
       (map set)
       (reduce #(+ %1 (count %2)) 0)))

(defn part2
  [f]
  (->> f
       read-answers
       (map #(str/split % #"\s+"))
       (map str-intersection)
       (reduce #(+ %1 (count %2)) 0)))
;; The End
