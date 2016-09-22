(ns tiny-maze.solver-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [tiny-maze.solver :refer :all]))

(facts "about turn movement" 
       (fact "moving right"
             (right [3 3] 0) => 1
             (right [3 3] 1) => 2
             (right [3 3] 2) => nil
             (right [3 3] 8) => nil)
       (fact "moving left"
             (left [3 3] 0) => nil
             (left [3 3] 1) => 0
             (left [3 3] 2) => 1
             (left [3 3] 8) => 7)
       (fact "moving up"
             (up [3 3] 0) => 3
             (up [3 3] 1) => 4
             (up [3 3] 2) => 5
             (up [3 3] 8) => nil)
       (fact "moving up"
             (down [3 3] 0) => nil
             (down [3 3] 1) => nil
             (down [3 3] 7) => 4
             (down [3 3] 8) => 5)
       )
 (facts "about the maze"
        (start [0 0 :S 0 nil 0 :x :E]) => 2
        (end   [0 0 :S 0 nil 0 :x :E]) => 7
        )

(deftest test-solve-maze
  (testing "can find way to exit with 3x3 maze"
    (let [maze [[:S 0 1]
                [1  0 1]
                [1  0 :E]]
          sol [[:x :x 1]
               [1  :x 1]
               [1  :x :x]]]
      (is (= sol (solve-maze maze)))))

    (testing "can find way to exit with 4x4 maze"
    (let [maze [[:S 0 0 1]
                [1  1 0 0]
                [1  0  0 1]
                [1  1  0 :E]]
          sol [[:x :x :x 1]
                [1  1 :x 0]
                [1  0 :x 1]
                [1  1  :x :x]]]
     (is (= sol (solve-maze maze))))))
