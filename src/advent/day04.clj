;; day04.clj

(ns advent.day04
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [spec-dict :refer [dict]]))

;;----------------
(defn read-passports
  "Read passport data."
  [f]
  (as-> f <>
      (slurp <>)
      (str/split <> #"\n\n")
      (map #(str/replace % "\n" " ") <>)))

;; map-item :: String -> Map Keyword String
(defn- map-item
  "Turn two strings separated by a colon into a key-value map item."
  [s]
  (let [[k v] (str/split s #":")]
    {(keyword k) v}))

(defn parse-data
  "Turn a string of pairs into maps."
  [s]
  (let [s1 (str/split s #"\s+")]
    (into {} (map map-item s1))))

(defn safeParseInt
  "Returns 0 if not an integer, rather than throw an error."
  [s]
  (if (nil? (re-matches #"[0-9]+" s))
    0
    (Integer/parseInt s)))

;;----------------
(s/def ::height
  (fn [s]
   (let [p (- (count s) 2)
         value (safeParseInt (subs s 0 p))]
     (case (subs s p)
       "in" (<= 59 value 76)
       "cm" (<= 150 value 193)
       false))))

(s/def ::rgb #(not (nil? (re-matches #"#[0-9a-f]{6}" %))))

(s/def ::eye-colour #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"})

(s/def ::passport
  (dict {:byr #(<= 1920 (safeParseInt %) 2002)
         :iyr #(<= 2010 (safeParseInt %) 2020)
         :eyr #(<= 2020 (safeParseInt %) 2030)
         :hgt ::height
         :hcl ::rgb
         :ecl ::eye-colour
         :pid #(not (nil? (re-matches #"[0-9]{9}" %)))}
         ^:opt {:cid string?}))

(defn valid-passport?
  [p]
  (s/valid? ::passport (parse-data p)))

(defn validate-passports
  [f]
  (->> f
       read-passports
       (map valid-passport?)
       (filter true?)
       count))

;;----------------
;; Input files
(def test1 "data/day04-test.txt")
(def test2 "data/day04-test2.txt")
(def input "data/day04-input.txt")

(println (validate-passports input))

;; The End
