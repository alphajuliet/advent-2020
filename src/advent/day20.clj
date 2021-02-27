(ns advent.day20
  (:require [advent.util :as util]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [meander.epsilon :as me]))

;;----------------
(defn bin->dec [b] (Integer/parseInt b 2))

(defn bin-rev
  "Reverse the number in 10-bit binary space."
  [n]
  (->> n
       (pp/cl-format nil "~10,'0b")
       str/reverse
       bin->dec))

;;----------------
(defn tile-edges
  "Turn the tile into an ordered collection of decimal representations
  of the edges, followed by their bit-reversed versions."
  [t]
  (let [number (Integer/parseInt (re-find #"\d+" (first t)))
        e-top    (->> t second bin->dec)
        e-right  (->> t rest util/T last (str/join "") bin->dec)
        e-bottom (->> t last str/reverse bin->dec)
        e-left   (->> t rest util/T first reverse (str/join "") bin->dec)
        edges [e-top e-right e-bottom e-left]]
    {:tile number
     :edges (into edges (map bin-rev edges))}))

(defn tile-edges-2
  "Turn the tile into an ordered collection of decimal representations
  of the edges."
  [t]
  (let [number (Integer/parseInt (re-find #"\d+" (first t)))
        e-top    (->> t second bin->dec)
        e-right  (->> t rest util/T last (str/join "") bin->dec)
        e-bottom (->> t last str/reverse bin->dec)
        e-left   (->> t rest util/T first reverse (str/join "") bin->dec)]
    {:tile number
     :edges [e-top e-right e-bottom e-left]}))

(defn read-tiles
  "Read in the tile and convert to binary strings."
  [f]
  (->> f
       util/import-data
       (partition-by (comp zero? count))
       (filter (comp #(< 1 %) count))
       (util/mapmap #(str/replace % "." "0"))
       (util/mapmap #(str/replace % "#" "1"))))

;;----------------
(defn tupleize
  "Turn a tile edge map into a collection of tuples, each with the tile and an edge."
  [m]
  (into [] (map #(vector (:tile m) %) (:edges m))))

(defn tupleize-2
  [m]
  (into [] (map #(assoc m :edge %) (:edges m))))

(defn rotate-tile
  [t]
  (update t :edges #(util/rotate -1 %)))

(defn flip-tile
  "Flip a tile around the vertical axis by bit-reversing all the edges, and
  swapping the left and right edges."
  [t]
  (-> t
      (update :edges #(mapv bin-rev %))
      (update :edges #(util/swap-elements % 1 3))))

(defn orientations
  "Generate and label all 8 edge orientations of a tile."
  [t]
  (let [r rotate-tile
        s flip-tile
        fns [identity r (comp r r) (comp r r r)
             s (comp r s) (comp r r s) (comp r r r s)]]
    (->> t
         ((apply juxt fns))
         (map #(assoc %2 :orient %1) (range 8)))))

;;----------------
(defn part1
  "Functional pipeline to manipulate the tiles into an answer."
  [f]
  (->> f
       read-tiles
       (map (comp tupleize tile-edges))
       (apply concat)
       (sort-by second)
       (partition-by second)
       (filter #(> (count %) 1))
       (apply concat)
       (group-by first)
       (filter #(= (count (val %)) 4))
       keys
       (apply *)))

(def testf "data/day20-test.txt")
(def inputf "data/day20-input.txt")


;;----------------
;; Part 2
(def z (->> testf read-tiles (map tile-edges-2)))
(def y (->> z (map orientations) (apply concat)))

(defn part2
  [f]
  (->> f
       read-tiles
       (map (comp orientations tile-edges-2))))

;; The End
