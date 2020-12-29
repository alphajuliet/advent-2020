;; day08.clj
;; You can tell I'm an electrical engineer by training.

(ns advent.day08
  (:require [advent.util :as util]
            [clojure.string :as str]))

;;----------------
(defn convert-args
  "Convert the first arg to a number."
  [v]
  (update v 1 #(Integer/parseInt %)))

(defn read-code
  "Read instructions from a file."
  [f]
  (->> f
       util/import-data
       (map #(str/split % #"\s+"))
       (map convert-args)))

;;----------------
(defn exec-instr
  "Execute each instruction, which is an opcode and an argument."
  [[op arg] state]
  (let []
    (case op
      "nop" (-> state
                (update :visited #(conj % (:pc state)))
                (update :pc inc))
      "acc" (-> state
                (update :acc #(+ % arg))
                (update :visited #(conj % (:pc state)))
                (update :pc inc))
      "jmp" (-> state
                (update :visited #(conj % (:pc state)))
                (update :pc #(+ % arg)))
      :default (throw (AssertionError. "Unrecognised opcode")))))

(defn run-code
  "Main execution loop."
  [code]
  (let [initial-state {:pc 0 :acc 0 :visited [] :exit :timeout}
        max-instrs 1000]
    (reduce
     (fn [state _]
       (cond
         ;; If normal termination
         (>= (:pc state) (count code))
         ;; then
         (reduced (into state {:exit :normal}))

         ;; If we've executed this instruction before
         (some #(= % (:pc state)) (:visited state))
         ;; then
         (reduced (into state {:exit :loop}))

         ;;else execute the current instruction at the program counter
         :else (exec-instr (nth code (:pc state)) state)))

     initial-state
     (range max-instrs))))

(defn run-file
  "Execute the program in the file."
  [f]
  (->> f
       read-code
       run-code))

;;----------------
(defn filter-nop-jmp
  "Filter the line numbers of executed nop or jmp instructions."
  [code state]
  (filter #(re-matches #"nop|jmp" (first (nth code %)))
          (:visited state)))

(defn patch-instr
  "Patch the code at the given line number."
  [code line-number]
  (update-in (vec code) [line-number 0]
             #(case %
                "nop" "jmp"
                "jmp" "nop")))

(defn try-patch-all
  "Brute force execution of changing all nop and jmp instructions."
  [code]
  (let [nop-or-jmp (filter-nop-jmp code (run-code code))]
    (map #(run-code (patch-instr code %))
         nop-or-jmp)))

;;----------------
(def testf "data/day08-test.txt")
(def testf1 "data/day08-test1.txt")
(def inputf "data/day08-input.txt")

(defn part1
  [f]
  (-> f
      run-file
      :acc))

(defn part2
  [f]
  (->> f
       read-code
       try-patch-all
       (filter #(= :normal (:exit %)))
       first
       :acc))

;; The End
