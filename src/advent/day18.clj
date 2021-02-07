(ns advent.day18
  (:require [advent.util :as util]
            [clojure.string :as str]
            [instaparse.core :as insta]))

;;----------------
(defn read-expressions
  [f]
  (->> f util/import-data))

;;----------------
;; Define the grammar for part 1
(def gr-1
  (insta/parser
   "expr = element
   <element> = term | add | mul
   add = element <'+'> term
   mul = element <'*'> term
   <term> = number | <'('> element <')'>
   number = #'\\d+';"))

;; And part 2
(def gr-2
  (insta/parser
   "expr = element
   <element> = add-elt | mul
   mul = element <'*'> add-elt
   <add-elt> = term | add
   add = add-elt <'+'> term
   <term> = number | <'('> element <')'>
   number = #'\\d+';"))

;; Transform to simplify the parse tree.
(def xf
  {:number #(Integer/parseInt %)
   :add +
   :mul *
   :expr identity})

(defn evaluate
  "Parse and evaluate a string using the given parser."
  [p s]
  (->> (str/replace s #"\s+" "")
       p
       (insta/transform xf)))

;;----------------
(def inputf "data/day18-input.txt")

(defn part1
  [f]
  (->> f
       read-expressions
       (map (partial evaluate gr-1))
       (apply +)))

(defn part2
  [f]
  (->> f
       read-expressions
       (map (partial evaluate gr-2))
       (apply +)))

;; The End
