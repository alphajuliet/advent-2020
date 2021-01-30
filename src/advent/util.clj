;; util.clj

(ns advent.util
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]))

(defn swap [f x y] (f y x))

;; filter-if :: ∀ a. List Boolean -> List a -> List a
(defn filter-if
  "Filter c2 according to the truth of the corresponding element in c1.
  (filter-if [true false true] [1 2 3]) => [1 3]"
  [c1 c2]
  (remove nil? (map #(if %1 %2) c1 c2)))

;; Transpose a list of lists
(def T (partial apply map list))

;; number-of-bits :: Number -> Integer
(defn number-of-bits
  "Number of bits required to store `n`."
  [n]
  (if (zero? n) 0
      (-> (Math/log n)
            (/ (Math/log 2))
            inc
            Math/floor
            int)))

;; import-data :: IO File -> List String
(defn import-data
  "Import and prepare the data"
  [f]
  (->> f
       (slurp)
       (str/split-lines)))

(defn leftpad
  "If S is shorter than LEN, pad it with CH on the left."
  ([s len] (leftpad s len " "))
  ([s len ch]
   (pp/cl-format nil (str "~" len ",'" ch "d") (str s))))

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
