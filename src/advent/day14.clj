;; day14.clj

(ns advent.day14
  (:require [advent.util :as util]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(import java.math.BigInteger)

(defn parse-line
  [s]
  (if (str/starts-with? s "mask")
    (re-seq #"^(\w+)\s=\s([01X]+)$" s)
    ;; else
    (re-seq #"^(\w+)\[(\d+)\]\s=\s(\d+)$" s)))

(defn read-commands
  [f]
  (->> f
       util/import-data
       (map (comp rest flatten parse-line))))

(defn apply-mask
  "Apply the given mask to all 36 bits of `x`"
  [x mask]
  (let [xstr (util/leftpad (Integer/toBinaryString x) 36 "0")
        xlst (str/split xstr #"")
        mlst (str/split mask #"")
        outstr (apply str (map #(if (= "X" %2) %1 %2) mlst xlst))]
    (BigInteger. outstr 2)))

(defn apply-mask-2
  "Apply the mask in `y` to `x`."
  [x y]
  (let [xstr (util/leftpad (Integer/toBinaryString x) 36 "0")
        xlst (str/split xstr #"")
        ylst (str/split y #"")]
    (apply str (map #(if (= "0" %2) %1 %2) xlst ylst))))

(defn mask-gen
  "Return all values of a mask with 'floating' X values, but only up to `n` bits.
  i.e. define the 'corners' of the binary n-cube that includes all the floating bits.
  e.g. `(mask-gen 4 'XX0X1X') => (2 3 6 7)`"
  [mask]
  (let [min-m (BigInteger. (str/replace mask "X" "0") 2)]
    (->> (str/split mask #"")
         reverse
         (keep-indexed #(if (= "X" %2) %1))
         (map #(bit-shift-left 1 %))
         combo/subsets
         (map #(apply + %))
         (map #(BigInteger/valueOf %))
         (map #(.or min-m %))
         (sort <))))

(defn exec-instr
  "Execute a single instruction `i` on the current state `st`."
  [state i]
  (if (= "mask" (first i))
    ;; Set up the mask
    (assoc state :mask (second i))
    ;;else write to the memory location
    (assoc state (nth i 1) (apply-mask bit-mask
                                       (Integer/parseInt (nth i 2))
                                       (:mask state)))))

(defn exec-instr-2
  "Execute a single instruction in part 2."
  [state i]
  (if (= "mask" (first i))
    ;; save the current mask
    (assoc state :mask (second i))
    ;; else
    (let [address (Integer/parseInt (nth i 1))
          value (Integer/parseInt (nth i 2))
          mask (apply-mask-2 address (:mask state))
          addresses (mask-gen mask)]
      (reduce
       (fn [st adrs]
         (assoc st adrs value))
       state
       addresses))))

(defn execute
  "Execute a series of instructions on an empty memory."
  [model-fn instrs]
  (reduce model-fn {} instrs))

(defn sum-memory
  [m]
  (apply + (vals (dissoc m :mask))))

;;----------------
(def testf "data/day14-test.txt")
(def testf2 "data/day14-test2.txt")
(def inputf "data/day14-input.txt")

(defn run
  [f exec-fn]
  (->> f
       read-commands
       (execute exec-fn)
       sum-memory))

(defn part1 [f] (run f exec-instr))
(defn part2 [f] (run f exec-instr-2))

;; The End
