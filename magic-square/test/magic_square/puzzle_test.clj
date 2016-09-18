(ns magic-square.puzzle-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [ clojure.tools.logging :as log] 
            [magic-square.puzzle :refer :all]))

(defn sum-rows [m]
  (map #(reduce + %) m))

(defn sum-cols [m]
  [(reduce + (map first m))
   (reduce + (map second m))
   (reduce + (map last m))])

(defn sum-diagonals [m]
  [(+ (get-in m [0 0]) (get-in m [1 1]) (get-in m [2 2]))
   (+ (get-in m [2 0]) (get-in m [1 1]) (get-in m [0 2]))])

(facts "test magic square"
       (fact "run it" (log/spy :info (magic-square values)) => truthy
       ; (fact "all the rows, columns, and diagonal add to the same number"
       ;       (set (sum-rows (magic-square values)))      => (set (sum-cols (magic-square values)))
       ;       (set (sum-diagonals (magic-square values))) => (set (sum-rows (magic-square values)))

       ;       (count (set (sum-rows (magic-square values)))) => 1
       ;       (count (set (sum-cols (magic-square values)))) => 1
       ;       (count (set (sum-diagonals (magic-square values)))) => 1
        ))
