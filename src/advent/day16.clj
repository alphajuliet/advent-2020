;; day16.clj

(ns advent.day16
  (:require [advent.util :as util]
            [clojure.core.matrix :as m]
            [clojure.set :as set]
            [clojure.string :as str]))

;;----------------
;; list-intersection :: ∀ a. List (List a) -> Set a
(defn list-intersection
  "Apply an intersection to a set of lists."
  [lst]
  (apply set/intersection (map set lst)))

;;----------------
;; read-ticket :: IO File -> List (List String)
(defn read-notes
  "Read in the notes and split into the three sections."
  [f]
  (->> f
       util/import-data
       (partition-by (comp zero? count))
       (filter (comp (partial < 1) count))))

;; get-rules :: String -> List Number
(defn get-rules
  "Parse the validation entry into a vector with the two range limits.
  e.g. `(get-pattern \"class: 1-3 or 5-7\")` => '(1 3 5 7)`"
  [s]
  (->> s
       (re-matches #"^[a-z ]+:\s+(\d+)-(\d+) or (\d+)-(\d+)")
       rest
       (map #(Integer/parseInt %))))

(defn get-field-names
  "Return the names of the rules."
  [s]
  (->> s
       (re-matches #"^([a-z ]+):.+")
       second))

;; make-validator :: String -> (Number -> Boolean)
(defn make-validator
  "Return validation for either of the ranges."
  [s]
  (let [[a b c d] (get-rules s)]
    (fn [x] (or (<= a x b)
                (<= c x d)))))

;;----------------
;; get-tickets :: String -> List Number
(defn get-ticket-data
  "Get all the ticket data in the given string."
  [s]
  (->> (str/split s #",")
       (map #(Integer/parseInt %))))

;; process-tickets :: List String -> List (List Number)
(defn process-tickets
  "Get all the numbers from the collection of tickets."
  [coll]
  (map get-ticket-data (rest coll)))

;;----------------
;; valid-ticket? :: List (Number -> Boolean) -> List Number -> Number
(defn invalid-ticket?
  "Check a ticket against the invalidators and return the invalid number."
  [ivs ticket]
  (->> ivs
       (map #(remove % ticket))
       (map set)
       (apply set/intersection)
       vec
       first))

(defn valid-ticket?
  "Determine ticket validity against all the rules."
  [vs ticket]
  (->> ticket
       (map (fn [x] (some? (some #(% x) vs))))
       (every? true?)))

(defn validation-results
  [vs ticket]
  (->> ticket
       (map (fn [x] (map #(if (% x) 1 0) vs)))))

(defn valid-counts
  [vs ticket]
  (map (fn [n] (util/count-if true? (map #(% n) vs))) ticket))

(defn validation-labels
  "Return the labels of all the validators that pass."
  [vs labels ticket]
  (->> ticket
       (map (fn [x] (map #(% x) vs)))
       (map #(util/filter-if % labels))))

;;----------------
(def testf "data/day16-test.txt")
(def testf2 "data/day16-test2.txt")
(def inputf "data/day16-input.txt")

(defn part1
  [f]
  (let [[rules _ nearby-tickets] (read-notes f)
        validators (map make-validator rules)]
    (->> (process-tickets nearby-tickets)
         (map #(invalid-ticket? validators %))
         (remove nil?)
         (apply +))))

(defn part2
  [f]
  (let [[rules my-ticket nearby-tickets] (read-notes f)
        validators (map make-validator rules)
        labels (map get-field-names rules)
        mine (process-tickets my-ticket)
        tickets (process-tickets nearby-tickets)]
    (->> tickets
         (map #(valid-ticket? validators %)))))

;;----------------
;; Working

(def z (read-notes inputf))
(def vs (map make-validator (first z)))
(def labels (map get-field-names (first z)))
(def y (->> z
            last
            process-tickets
            (filter (partial valid-ticket? vs))))

;; Generate a 3-d tensor of tickets x ticket numbers x validation results
(def m3 (->> y
             (map (partial validation-results vs))
             (m/array)))

;; The End
