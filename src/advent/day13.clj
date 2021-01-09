;; day13.clj

(ns advent.day13
  (:require [advent.util :as util]
            [clojure.string :as str]))

;;----------------
;; Utility functions

(defn argmin
  [f coll]
  (apply min-key f coll))

(defn mod-compl
  "The complement of the remainder."
  [num div]
  (- div (mod num div)))

;;----------------
(def read-notes util/import-data)

(defn get-data
  "Extract the data from the notes."
  [[ts routes]]
  {:timestamp (Integer/parseInt ts)
   :routes (as-> routes <>
             (str/split <> #",")
             (filter #(not= % "x") <>)
             (map #(Integer/parseInt %) <>))})

(defn next-bus
  "Find the bus that has the shortest wait time.
  Return the bus number and the wait time as a vector."
  [ts coll]
  [(argmin #(mod-compl ts %) coll)
   (apply min (map (partial mod-compl ts) coll))])

;;----------------
(def testf "data/day13-test.txt")
(def inputf "data/day13-input.txt")

(defn go
  [f]
  (let [{:keys [timestamp routes]} (-> f read-notes get-data)]
    (apply * (next-bus timestamp routes))))

;; The End
