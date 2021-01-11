;; day14.clj

(ns advent.day14
  (:require [advent.util :as util]
            [clojure.string :as str]))


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

(defn bit-mask
  "Mask a single bit."
  [x mask]
  (if (= "X" mask)
    x
    mask))

(defn mask
  "Apply the given mask to all 36 bits of `x`."
  [x mask]
  (let [xstr (util/leftpad (Integer/toBinaryString x) 36 "0")
        xlst (str/split xstr #"")
        mlst (str/split mask #"")
        outstr (apply str (map bit-mask xlst mlst))]
    (BigInteger. outstr 2)))

(defn exec-instr
  "Execute a single instruction `i` on the current state `st`."
  [st i]
  (if (= "mask" (first i))
    ;; Set up the mask
    (assoc st :mask (second i))
    ;;else write to the memory location
    (assoc st (nth i 1) (mask (Integer/parseInt (nth i 2))
                              (:mask st)))))

(defn execute
  "Execute a series of instructions on an empty memory."
  [instrs]
  (reduce exec-instr {} instrs))

(defn sum-memory
  [m]
  (apply + (vals (dissoc m :mask))))

;;----------------
(def testf "data/day14-test.txt")
(def inputf "data/day14-input.txt")

(defn part1
  [f]
  (->> f
       read-commands
       execute
       sum-memory))

;; The End
