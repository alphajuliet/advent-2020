;; day07.clj

(ns advent.day07
  (:require [advent.util :as util]
            [clojure.string :as str]
            [ubergraph.alg :as alg]
            [ubergraph.core :as uber]))

;;----------------
(defn read-rules
  [f]
  (->> f
       util/import-data
       (map #(str/split % #"\scontain\s"))))

;;----------------
(defn roots
  [rules]
  (->> rules
       (filter #(= "no other bags." (second %)))
       (map first)))

(defn target-node
  "Extract the target node as a keyword."
  [rule]
  (as-> rule <>
    (first <>)
    (str/replace <> " bags" "")
    (str/split <> #"\s+")
    (str/join "-" <>)
    (keyword <>)))

(defn source-vector
  "Extract a vector of the source and its cardinality."
  [s]
  (let [c (str/split s #"\s+")
        n (Integer/parseInt (first c))
        src (keyword (str/join "-" (rest c)))]
    [src n]))

(defn source-nodes
  "Extract a list of source nodes and cardinalities from a single rule."
  [rule]
  (as-> rule <>
    (second <>)
    (str/replace <> #"\s+bag(s)?\.?" "")
    (str/split <> #",\s")
    (case <>
      ["no other"] []
      (into [] (map source-vector <>)))))

(defn edges
  "Turn a rule into a collection of edges."
  [rule]
  (let [target (target-node rule)
        sources (source-nodes rule)]
    (for [[s c] sources]
      [s target c])))

(defn all-edges
  "Generate all the edges in a file of rules."
  [rules]
  (->> rules
       (map edges)
       (reduce concat [])))

(defn create-graph
  "Turn a file of rules into a directed graph."
  [f]
  (->> f
       read-rules
       all-edges
       (uber/add-directed-edges* (uber/graph))))

;;----------------
(defn incoming
  [graph node]
  (map (partial uber/edge-with-attrs graph)
       (uber/in-edges graph node)))

;; Recursively sum over all the edge weights in the subgraph.
;; At each node, the sum S_node_n = ∀ incoming nodes i: w_i * (1 + S_i)))
;; where w_i is the weight of the incoming edge from node i.

(defn sum-incoming-weights
  "Recursive function to total all the incoming weights."
  [graph node]
  (let [incoming (incoming graph node)]
    (reduce #(+ %1 (* (uber/weight graph %2)
                      (inc (sum-incoming-weights graph (first %2)))))
            0
            incoming)))

;;----------------
(defn reachable
  [graph node]
  (let [paths (alg/shortest-path graph {:start-node node})]
    (alg/all-destinations paths)))

;;----------------
(def testf "data/day07-test.txt")
(def testf1 "data/day07-test1.txt")
(def inputf "data/day07-input.txt")

(defn part1
  [f]
  (as-> f <>
    (create-graph <>)
    (reachable <> :shiny-gold)
    (count <>)
    (dec <>)))

(defn part2
  [f]
  (-> f
      create-graph
      (sum-incoming-weights :shiny-gold)))

;; The End
