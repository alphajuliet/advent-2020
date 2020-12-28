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

(defn exec-instr
  "Execute each instruction, which is an opcode and an argument."
  [[op arg] state]
  (let []
    (case op
      "nop" (-> state
                (update :visited #(conj % (:pc state)))
                (update :ninstrs inc)
                (update :pc inc))
      "acc" (-> state
                (update :acc #(+ % arg))
                (update :visited #(conj % (:pc state)))
                (update :ninstrs inc)
                (update :pc inc))
      "jmp" (-> state
                (update :visited #(conj % (:pc state)))
                (update :ninstrs inc)
                (update :pc #(+ % arg)))
      :default (throw "Error: unknown opcode"))))

(defn run-code
  "Main execution loop."
  [code]
  (let [initial-state {:pc 0 :acc 0 :visited [] :ninstrs 0}
        max-instrs 1000]
    (reduce
     (fn [state i]
       (if (or (>= (:pc state) (count code))
               (some #(= % (:pc state)) (:visited state)))
         ;; halt if we either run out of code
         ;; or we've executed this instruction before.
         (reduced state)

         ;;else execute the current instruction at the program counter
         (exec-instr (nth code (:pc state)) state)))
     initial-state
     (range max-instrs))))

(defn run-file
  "Execute the program in the file."
  [f]
  (->> f
       read-code
       run-code))

;;----------------
(def testf "data/day08-test.txt")
(def inputf "data/day08-input.txt")

(defn part1
  [f]
  (-> f
      run-file
      :acc))

;; The End
