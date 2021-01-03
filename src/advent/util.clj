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

(defn count-if
  "Utility function"
  [f v]
  (count (filter f v)))

(defn take-until
  "Returns a lazy sequence of successive items from coll until
   (pred item) returns true, including that item. pred must be
   free of side-effects."
  [pred coll]
  (lazy-seq
    (when-let [s (seq coll)]
      (if (pred (first s))
        (cons (first s) nil)
        (cons (first s) (take-until pred (rest s)))))))

;; The End
