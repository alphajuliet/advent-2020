(ns advent.day17
  (:require [advent.util :as util]
            [clojure.core.matrix :as m]
            ;; [clojure.set :as set]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

;;----------------
(defn read-initial-state
  "Import the initial state as a binary matrix."
  [f]
  (->> f
       util/import-data
       (map #(str/replace % "#" "1"))
       (map #(str/replace % "." "0"))
       (map #(str/split % #""))
       (util/mapmap #(Integer/parseInt %))))

(defn new-grid-map
  "Create a sparse 3D tensor using a map."
  [matrix]
  (->> matrix
       m/array
       (m/emap-indexed list)
       (reduce concat [])
       (reduce #(if (pos? (last %2))
                  (into %1 {(conj (first %2) 0) (last %2)})
                  %1) {})))

(defn get-neighbours
  "Get the immediate neighbours at [x y z] in a sparse map.
  If nothing exists in a cell then return 0."
  [m [x y z]]
  (let [nn (->> (combo/selections [0 1 -1] 3)
                (remove (partial = [0 0 0])))]
    (map (fn [delta]
           (get m (map + [x y z] delta) 0))
         nn)))

(def count-neighbours
  (comp (partial util/count-if pos?) get-neighbours))

(defn rule
  "Apply the propagation rule to the element at xyz = [x y z]."
  [m xyz]
  (let [val (pos? (get m xyz 0))
        c (count-neighbours m xyz)]
    (if (or (= c 3)
            (and val (= c 2)))
      1 0)))

(defn coords
  "Generate a coordinate space of +/- n around the origin."
  [n]
  (combo/selections (range (- n) n) 3))

(defn boot-cycle
  "Run a single cycle over the grid within coords."
  [g n]
  (into {} (map #(if (pos? (rule g %))
                   {% 1} {})
                (coords n))))

(defn part1
  "Run for 6 iterations, and grow the grid as we go."
  [f]
  (let [input-grid (->> f read-initial-state)
        init-state (new-grid-map input-grid)
        size (count input-grid)]
    (count (reduce (fn [g iter] (boot-cycle g (+ size 1 iter)))
                   init-state
                   (range 6)))))

;;----------------
(def testf "data/day17-test.txt")
(def inputf "data/day17-input.txt")

;;(def z (read-initial-state testf))
;;(def y (new-grid-map z))

;; The End
