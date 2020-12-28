;; util.clj

(ns advent.util
  (:require [clojure.string :as str]))

(defn swap [f x y] (f y x))

(defn import-data
  "Import and prepare the data"
  [f]
  (->> f
       (slurp)
       (str/split-lines)))

(defn rotate
  "Rotate s by n to the left. If n is negative rotates to the right."
  [n s]
  (let [shift (mod n (count s))]
    (concat (drop shift s)
            (take shift s))))

(defn rotate-string
  "Rotate a string."
  [n s]
  (apply str (rotate n s)))

;; The End
