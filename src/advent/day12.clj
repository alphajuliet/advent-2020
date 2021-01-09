;; day12.clj

(ns advent.day12
  (:require [advent.util :as util]))

(defn d2r
  "Degrees to radians"
  [deg]
  (* (/ Math/PI 180.) deg))

(defn rotate
  "Rotate [x y] around the origin by theta degrees."
  [[x y] theta]
  (let [tr (d2r theta)]
    [(- (* x (Math/cos tr))
        (* y (Math/sin tr)))
     (+ (* x (Math/sin tr))
        (* y (Math/cos tr)))]))

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
  [{:keys [x y]}]
  (+ (Math/abs x)
     (Math/abs y)))

(defn exec-instr
  "Execute an instruction on a given state."
  [[instr n] {:keys [x y h xw yw] :as state}]
  (case instr
    "N" (update state :yw #(+ % n))
    "E" (update state :xw #(+ % n))
    "S" (update state :yw #(- % n))
    "W" (update state :xw #(- % n))

    "L" (-> state
            (assoc :xw (Math/round (first (rotate [xw yw] n))))
            (assoc :yw (Math/round (second (rotate [xw yw] n)))))
    "R" (-> state
            (assoc :xw (Math/round (first (rotate [xw yw] (Math/negateExact n)))))
            (assoc :yw (Math/round (second (rotate [xw yw] (Math/negateExact n))))))

    "F" (-> state
            (assoc :x (+ x (* n xw)))
            (assoc :y (+ y (* n yw))))))

(defn navigate
  "Navigate according to the directions and the position of the waypoint."
  [instrs]
  (let [init-state {:x 0 :y 0 :h 0
                    :xw 10 :yw 1}]
    (reduce
     (fn [state instr]
       (exec-instr instr state))
     init-state
     instrs)))

;;----------------
(def testf "data/day12-test.txt")
(def inputf "data/day12-input.txt")

(defn go
  [f]
  (->> f
       read-directions
       navigate
       manhattan-distance))

;; The End
