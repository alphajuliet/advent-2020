;; day11.clj

(ns advent.day11
  (:require [advent.util :as util]
            [clojure.core.matrix :as m]
            [clojure.string :as str]))

;;----------------
(def testf "data/day11-test.txt")
(def inputf "data/day11-input.txt")

(defn count-if
  [f v]
  (count (filter f v)))

;;----------------
(defn encode-row
  "Encode the row string as integers.
  0 for no seat, 1 for unoccupied, 2 for occupied."
  [s]
  (as-> s <>
       (str/replace <> #"\." "0")
       (str/replace <> #"L" "1")
       (str/split <> #"")
       (map #(Integer/parseInt %) <>)))

(defn read-layout
  "Read the layout into a integer matrix."
  [f]
  (->> f
       util/import-data
       (map encode-row)
       m/matrix))

(defn getm
  "A safe mget. Anything outside the matrix will be zero."
  [mat r c]
  (let [[maxr maxc] (m/shape mat)]
    (if (or (< r 0) (< c 0) (>= r maxr) (>= c maxc))
      0
      ;;else
      (m/mget mat r c))))

(defn neighbours
  "Get all the neighbours."
  [mat r c]
  (let [n [[1 0] [1 1] [0 1] [-1 1] [-1 0] [-1 -1] [0 -1] [1 -1]]]
    (map #(getm mat (+ r (first %)) (+ c (second %))) n)))

;;----------------
;; Rules

(defn all-unoccupied?
  "All neighbours unoccupied?"
  [v]
  (= 0 (count-if #(= 2 %) v)))

(defn too-many-occupied?
  "Too many neighbours occupied?"
  [v]
  (<= 4 (count-if #(= 2 %) v)))

(defn apply-rules
  "Apply the rules to an element with the given neighbours."
  [e n]
  (case e
    1 (if (all-unoccupied? n) 2 1)
    2 (if (too-many-occupied? n) 1 2)
    ;;else
    e))

(defn apply-all
  "Apply the rules to every element."
  [mat]
  (m/emap-indexed
   (fn [[r c] e] (apply-rules e (neighbours mat r c)))
   mat))

(defn run-apply
  "Run until nothing changes."
  [mat]
  (let [max-iter 200]
    (reduce
     (fn [m _]
       (let [newmat (apply-all m)]
         (if (m/equals newmat m)
           (reduced m)
           ;;else
           newmat)))
     mat
     (range max-iter))))

(defn part1
  [f]
  (->> f
       read-layout
       run-apply
       m/eseq
       (filter #(= 2 %))
       count))

  ;; The End
