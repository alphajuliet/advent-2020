;; day15.clj

(ns advent.day15)

(defn rule
  "Naive rule for creating the sequence."
  [s i]
  (let [idx (.lastIndexOf (pop s) (peek s))]
    (if (neg? idx)
      (conj s 0)
      (conj s (dec (- i idx))))))

(defn rule-2
  "Generate the sequence but use a map to store the last occurrence of each number.
  The map contains the numbers as keys, plus :last as the most recent number."
  [s i]
  (let [x (:last s)]
    (if (contains? s x)
      ;; Update the most recent occurrence of x
      (-> s
          (assoc x (dec i))
          (assoc :last (dec (- i (get s x)))))
      ;; else
      (-> s
          (assoc x (dec i))
          (assoc :last 0)))))

(defn generate
  "Run the sequence for a max number of iterations."
  [v limit]
  (let [coll (reduce rule v (range (count v) limit))]
          (subvec coll (- (count coll) 10))))

(defn generate-2
  "Run the sequence using a hash."
  [v limit]
  (let [m (zipmap (pop v) (range (count v)))
        out-map (reduce rule-2
                        (assoc m :last (peek v))
                        (range (count v) limit))]
    (:last out-map)))


;;----------------
(def testv [0 3 6])
(def inputv [7 14 0 17 11 1 2])

(defn part1 [] (generate inputv 2020))
(defn part2 [] (generate-2 inputv 30000000))

;; The End
