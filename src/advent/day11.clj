;; day11.clj

(ns advent.day11
  (:require [advent.util :as util]
            [clojure.core.matrix :as m]
            [clojure.string :as str]))

;;----------------
(def testf "data/day11-test.txt")
(def inputf "data/day11-input.txt")

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

;;----------------
(defn in-matrix?
  "Are we still inside the matrix?"
  [mat r c]
  (let [[maxr maxc] (m/shape mat)]
    (and (nat-int? r) (nat-int? c) (< r maxr) (< c maxc))))

(defn getm
  "A safe mget. Anything outside the matrix will be zero."
  [mat r c]
  (if (in-matrix? mat r c)
      (m/mget mat r c)
      0))

(defn neighbours
  "Get all the neighbours."
  [mat r c]
  (let [n [[1 0] [1 1] [0 1] [-1 1] [-1 0] [-1 -1] [0 -1] [1 -1]]]
    (map (fn [[dr dc]]
           (getm mat (+ r dr) (+ c dc)))
         n)))

(defn ray-seq
  "Return an infinite (lazy) sequence of coordinates starting at [row col] and
  incrementing by [dr dc]."
  [row col dr dc]
  (iterate (fn [[r c]] [(+ r dr) (+ c dc)])
           [row col]))

(defn first-ray-value
  "Return the first element along the direction of the ray [dr dc] from [row col]"
  [mat row col dr dc]
  (->> (ray-seq row col dr dc)
       rest
       (util/take-until
        (fn [[r c]]
          (not (and (in-matrix? mat r c)
                    (zero? (getm mat r c))))))
       last
       (apply (partial getm mat))))

(defn ray-neighbours
  "The ray version of neighbours"
  [mat row col]
  (let [n [[1 0] [1 1] [0 1] [-1 1] [-1 0] [-1 -1] [0 -1] [1 -1]]]
    (map (fn [[dr dc]]
           (first-ray-value mat row col dr dc))
         n)))

;;----------------
;; Rules

(defn all-unoccupied?
  "All neighbours unoccupied?"
  [v]
  (zero? (util/count-if #(= 2 %) v)))

(defn too-many-occupied?
  "Too many neighbours occupied?
  This is 4 for part 1 and 5 for part2!"
  [v]
  (<= 4 (util/count-if #(= 2 %) v)))

(defn too-many-occupied-2?
  "Too many neighbours occupied?
  This is 5 for part2!"
  [v]
  (<= 5 (util/count-if #(= 2 %) v)))

;;----------------
(defn apply-rules
  "Apply the rules to an element with the given neighbours."
  [e n]
  (case e
    1 (if (all-unoccupied? n) 2 1)
    2 (if (too-many-occupied? n) 1 2)
    ;;else
    e))

(defn apply-rules-2
  "Apply the part 2 rules to an element with the given neighbours."
  [e n]
  (case e
    1 (if (all-unoccupied? n) 2 1)
    2 (if (too-many-occupied-2? n) 1 2)
    ;;else
    e))

;;----------------
(defn apply-all
  "Apply the rules to every element."
  [f mat]
  (m/emap-indexed
   (fn [[r c] e]
     (f e (neighbours mat r c)))
   mat))

(defn apply-all-2
  "Apply the part 2 rules to every element."
  [f mat]
  (m/emap-indexed
   (fn [[r c] e]
     (f e (ray-neighbours mat r c)))
   mat))

;;----------------
(defn run-apply
  "Run until nothing changes."
  [mat]
  (let [max-iter 200]
    (reduce
     (fn [m _]
       (let [newmat (apply-all apply-rules m)]
         (if (m/equals newmat m)
           (reduced m)
           ;;else
           newmat)))
     mat
     (range max-iter))))

(defn run-apply-2
  "Run until nothing changes."
  [mat]
  (let [max-iter 200]
    (reduce
     (fn [m _]
       (let [newmat (apply-all-2 apply-rules-2 m)]
         (if (m/equals newmat m)
           (reduced m)
           ;;else
           newmat)))
     mat
     (range max-iter))))

;;----------------
(defn part1
  [f]
  (->> f
       read-layout
       run-apply
       m/eseq
       (filter #(= 2 %))
       count))

(defn part2
  [f]
  (->> f
       read-layout
       run-apply-2
       m/eseq
       (filter #(= 2 %))
       count))

  ;; The End
