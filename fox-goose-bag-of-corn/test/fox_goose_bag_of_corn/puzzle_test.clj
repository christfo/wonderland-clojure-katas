(ns fox-goose-bag-of-corn.puzzle-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [fox-goose-bag-of-corn.puzzle :refer :all]
            [clojure.set]))

(defn validate-move [step1 step2]
  (testing "only you and another thing can move"
    (let [diff1 (clojure.set/difference step1 step2)
          diff2 (clojure.set/difference step2 step1)
          diffs (concat diff1 diff2)
          diff-num (count diffs)]
      (is (> 3 diff-num))
      (when (pos? diff-num)
        (is (contains? (set diffs) :you)))
      step2)))

(deftest test-river-crossing-plan
  ; (let [crossing-plan (map (partial map set) (river-crossing-plan))]
  (let [crossing-plan ( river-crossing-plan )]
  (println "solution ------------------------" )
    (ppath crossing-plan)
    (testing "you begin with the fox, goose and corn on one side of the river"
      (is (= [#{:you :fox :goose :corn} #{:boat} #{}]
             (first crossing-plan))))
    (testing "you end with the fox, goose and corn on one side of the river"
      (is (= [#{} #{:boat} #{:you :fox :goose :corn}]
             (last crossing-plan))))
    (testing "things are safe"
      (let [left-bank (map first crossing-plan)
            right-bank (map last crossing-plan)]
        (testing "the fox and the goose should never be left alone together"
          (is (empty?
               (filter #(= % #{:fox :goose}) (concat left-bank right-bank)))))
        (testing "the goose and the corn should never be left alone together"
          (is (empty?
               (filter #(= % #{:goose :corn}) (concat left-bank right-bank)))))))
    (testing "The boat can carry only you plus one other"
      (let [boat-positions (map second crossing-plan)]
        (is (empty?
             (filter #(> (count %) 3) boat-positions)))))
    (testing "moves are valid"
      (let [left-moves (map first crossing-plan)
            middle-moves (map second crossing-plan)
            right-moves (map last crossing-plan)]
        (reduce validate-move left-moves)
        (reduce validate-move middle-moves)
        (reduce validate-move right-moves )))))

(def test_state [#{:a :b :c :you} #{:d :e :boat} #{:f :g}])

(facts "about things in places"
       (fact "lh"
             (lhbank test_state) => #{:a :b :c :you}
             (cargo (lhbank test_state) ) => #{:a :b :c nil})
       (fact "rh"
             (rhbank test_state) => #{:f :g}
             (cargo (rhbank test_state )) => #{:f :g nil}
             (cargo (rhbank test_state )) => #{:f :g nil})
       (fact "boat"
             (boat test_state) => #{:d :e :boat}
             (cargo (boat test_state )) => #{:d :e nil}) )

(facts "about game steps"
       ; (def test_state [#{:a :b :c :you} #{:d :e :boat} #{:f :g}])
       (fact "lbank to boat"
             (moves [#{ :andy :you } #{:boat  :bob } #{}] ) => (just [ #{:andy} #{:boat :you :bob} #{} ] 
                                                                     [ #{} #{:boat :andy :you :bob} #{} ] ) 
             ; (moves [#{ :fox :corn :you } #{:boat} #{}] ) => ( contains [ #{:fox} #{:boat :you :corn} #{} ] ) 
             ))

(facts "about legal states"
       (fact "legal postions true"
             (legal [#{:corn } #{:you :boat :fox} #{:goose}] ) => truthy
             (legal [#{:corn :fox } #{:you :goose :boat} #{}] ) => truthy)
       (fact "boat too full"
             (legal [#{} #{:you :boat :corn :fox} #{:goose}] ) => falsey)
       (fact "too may you"
             (legal [#{:corn :you} #{:you :boat :fox} #{:goose}] ) => falsey)
       (fact "beached boat"
             (legal [#{:corn :boat} #{:you :fox} #{:goose}] )   => falsey)
       (fact "hungry goose"
             (legal [#{:corn :goose } #{:you :boat :fox} #{}] ) => falsey)
       (fact "hungry fox"
             (legal [#{:corn :you } #{:boat } #{:goose :fox}] ) => falsey)
       (fact "missing corn"
             (legal [#{:jim} #{:boat } #{:you :goose :fox}] ) => falsey)
       (fact "feast"
             (legal [#{:corn :fox :goose} #{:you :boat} #{}] ) => falsey)
       )

(facts "about solution?"
       (fact "is unsolved"
             (solved? [start-pos])         => falsey
             (solved? [:a start-pos])      => falsey
             (solved? [end-pos start-pos]) => falsey )
       (fact "is solved"
             (solved? [ end-pos ])           => truthy
             (solved? [ start-pos end-pos ]) => truthy )
       )


