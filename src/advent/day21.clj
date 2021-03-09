(ns advent.day21
  (:require [advent.util :as util]
            [clojure.string :as str]))

(defn decode-food
  [[i a]]
  {:ingredients i :allergens (rest a)})

(defn read-ingredients
  [f]
  (->> f
       util/import-data
       (mapv #(re-seq #"\w+" %))
       (mapv #(split-with (complement #{"contains"}) %))
       (mapv decode-food)))

(def testf "data/day21-test.txt")
(def inputf "data/day21-input.txt")

;; The End
