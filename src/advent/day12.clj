;; day12.clj
;;
(ns advent.day12
  (:require [advent.util :as util]))

(defn d2r
  "Degrees to radians"
  [deg]
  (* (/ Math/PI 180.) deg))

;;----------------
(defn convert-instr
  "Parse an instruction into something more useful."
  [s]
  (let [[_ h n] (first (re-seq #"(\w)(\d+)" s))]
    [h (Integer/parseInt n)]))

(defn read-directions
  "Read in the list of navigation instructions."
  [f]
  (->> f
       util/import-data
       (map convert-instr)))

;;----------------
(defn manhattan-distance
  [{:keys [x y _]}]
  (+ (Math/abs x)
     (Math/abs y)))

(defn exec-instr
  "Execute an instruction on a given state."
  [[instr n] {:keys [x y h]}]
  (case instr
    "N" {:x x :y (+ y n) :h h}
    "E" {:x (+ x n) :y y :h h}
    "S" {:x x :y (- y n) :h h}
    "W" {:x (- x n) :y y :h h}
    "L" {:x x :y y :h (mod (+ h n) 360)}
    "R" {:x x :y y :h (mod (- h n) 360)}
    "F" {:x (+ x (* n (Math/round (Math/cos (d2r h)))))
         :y (+ y (* n (Math/round (Math/sin (d2r h)))))
         :h h}))

(defn navigate
  "Navigate according to the directions."
  [instrs]
  (let [init-state {:x 0 :y 0 :h 0}]
    (reduce
     (fn [state instr]
       (exec-instr instr state))
     init-state
     instrs)))

;;----------------
(def testf "data/day12-test.txt")
(def inputf "data/day12-input.txt")

(defn part1
  [f]
  (->> f
       read-directions
       navigate
       manhattan-distance))

;; The End
