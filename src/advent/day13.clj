;; day13.clj

(ns advent.day13
  (:require [advent.util :as util]
            [clojure.string :as str]))

;;----------------
;; Utility functions

(defn argmin
  [f coll]
  (apply min-key f coll))

(defn mod-compl
  "The complement of the remainder."
  [num div]
  (- div (mod num div)))

(defn xgcd
  "Extended Euclidean Algorithm. Returns [gcd(a,b) x y] where ax + by = gcd(a,b)."
  [a b]
  (if (= a 0)
    [b 0 1]
    (let [[g x y] (xgcd (mod b a) a)]
      [g (- y (* (quot b a) x)) x])))

;;----------------
(def read-notes util/import-data)

(defn get-data
  "Extract the data from the notes."
  [[ts routes]]
  {:timestamp (Integer/parseInt ts)
   :routes (as-> routes <>
             (str/split <> #",")
             (replace {"x" "0"} <>)
             (map #(Integer/parseInt %) <>))})

(defn next-bus
  "Find the bus that has the shortest wait time.
  Return the bus number and the wait time as a vector."
  [ts coll]
  [(argmin #(mod-compl ts %) coll)
   (apply min (map (partial mod-compl ts) coll))])

(defn crt-step
  "An iteration of the CRT solution of $x = a (mod p) = b (mod q)$."
  [a b p q]
  (let [[_ u v] (xgcd p q)
        x (+ (* p b u) (* q a v))
        mn (* p q)]
    (mod x mn)))

(defn crt-solve
  "Solve using the CRT for vectors of residues `av` and moduli `mv`."
  [av mv]
  {:pre [(= (count av) (count mv))
         (>= (count av) 2)]}

  (let [initial [(bigint (first av)) (bigint (second av)) (bigint (first mv)) (bigint (second mv))]]
    (->> (range 2 (count av)) ; The first two are captured in the initial value.
         (reduce
          (fn [[a b p q] i]
            (let [x (crt-step a b p q)]
              [x (nth av i) (* p q) (nth mv i)]))
          initial)
         (apply crt-step))))

(defn get-residues
  "Return the vector of residues and moduli"
  [v]
  (let [x (reverse v)
        a (keep-indexed #(if (pos? %2) %1) x)
        m (filter pos? x)]
    [(map #(mod %1 %2) a m) m]))

;;----------------
(def testf "data/day13-test.txt")
(def inputf "data/day13-input.txt")

(defn part1
  [f]
  (let [{:keys [timestamp routes]} (-> f read-notes get-data)]
    (apply * (next-bus timestamp (filter pos? routes)))))

(defn part2
  [f]
  (let [v (->> f
               read-notes
               get-data
               :routes)
        [a m] (get-residues v)]
    (- (crt-solve a m) (dec (count v)))))

;; The End
