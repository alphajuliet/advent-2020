;; day16.clj

(ns advent.day16
  (:require [advent.util :as util]
            [clojure.string :as str]
            [clojure.set :as set]))

;;----------------
;; read-ticket :: IO File -> List (List String)
(defn read-ticket
  "Read in the ticket and split into the three sections."
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
(defn valid-ticket?
  "Check a ticket against the validators and return the invalid number."
  [vs ticket]
  (->> vs
       (map #(remove % ticket))
       (map set)
       (apply set/intersection)
       vec
       first))

;;----------------
(def testf "data/day16-test.txt")
(def inputf "data/day16-input.txt")

(defn part1
  [f]
  (let [[rules _ other-tickets] (read-ticket f)
        validators (map make-validator rules)
        tickets (process-tickets other-tickets)]
    (->> tickets
         (map #(valid-ticket? validators %))
         (remove nil?)
         (apply +))))

;; The End
