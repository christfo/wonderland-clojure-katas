(ns wonderland-number.finder
  (:require [ clojure.math.numeric-tower :as math]))

(defn match-digits? [n1 n2]  ( reduce = (map (comp set str) [n1 n2] )))
(defn wonderland? [n] (when  (every? #(match-digits? n (* n %)) [2 3 4 5 6]) n) ) 

(defn wonderland-list [digits]
  (let [lowest  (math/expt 10 (dec digits))
        highest (dec (math/expt 10 digits))] 
    (->> (range lowest highest)
         (map wonderland?)
         (keep identity))))

(defn wonderland-number 
  ( []         (wonderland-number 6))             ; question is about 6 digit wonderlands
  ( [digits]   (first (wonderland-list digits)))) ; apparently appending '0' to a wonderland is also a wonderland
   
