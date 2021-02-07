(ns advent.day18
  (:require [advent.util :as util]
            [clojure.string :as str]
            [instaparse.core :as insta]))

;;----------------
(defn read-expressions
  [f]
  (->> f util/import-data))

;;----------------
;; Define the grammar
(def gr
  (insta/parser
   "expr = element
   <element> = term | add | mul
   add = element <'+'> term
   mul = element <'*'> term
   <term> = number | <'('> element <')'>
   number = #'\\d+';"))

;; Transform to simplify the parse tree.
(def xf
  {:number #(Integer/parseInt %)
   :add +
   :mul *
   :expr identity})

(defn evaluate
  "Parse and evaluate the syntax tree."
  [s]
  (->> (str/replace s #"\s+" "")
       gr
       (insta/transform xf)))

;;----------------
(def inputf "data/day18-input.txt")

(defn part1
  [f]
  (->> f
       read-expressions
       (map evaluate)
       (apply +)))

;; The End
