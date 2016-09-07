(ns card-game-war.game-test 
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [card-game-war.game :refer :all]))


;; fill in  tests for your game

(deftest test-deal
  (testing "dealing the cards"
    (is (count (deal 1)) 1)
    (is (count (deal 2)) 2)
    (is (count (deal 3)) 3)
    (is (count (deal))   2)))
    
(deftest test-play-round
  (testing "the highest rank wins the cards in the round"
    (is (= 1 (play-round #{:diamond 8} #{:diamond 3}) ))
    (is (= 2 (play-round #{:diamond 3} #{:diamond 8}) )))
  (testing "queens are higher rank than jacks"
    (is (= 1 (play-round #{:diamond :queen} #{:diamond :jack}) )))
  (testing "kings are higher rank than queens"
    (is (= 1 (play-round #{:spade :king} #{:spade :queen}) )))
  (testing "aces are higher rank than kings"
    (is (= 1 (play-round #{:club :ace} #{:heart :king}) )))
  (testing "if the ranks are equal, clubs beat spades"
    (is (= 1 (play-round #{:club 8} #{:spade 8}) )))
  (testing "if the ranks are equal, diamonds beat clubs"
    (is (= 1 (play-round #{:diamond :ace} #{:club :ace}) )))
  (testing "if the ranks are equal, hearts beat diamonds"
    (is (= 1 (play-round #{:heart :jack} #{:diamond :jack}) ))))

(deftest test-play-game
  (testing "the player loses when they run out of cards"
    (is (= 1 (play-game [ #{:diamond 3} ]  [                ]  )))
    (is (= 2 (play-game [               ]  [ #{:club :king} ]  )))
    (is (= 2 (play-game [ #{:diamond 3} ]  [ #{:club :king} ]  )))
    (is (#{1 2} (play-game (deal))))
    ))


; try out facts...

(fact "dealing the cards splits them between players"
      (deal 1)                  => #(= 1 (count %))
      (deal 2)                  => #(= 2 (count %))
      (deal 3)                  => #(= 3 (count %))
      (deal)                    => #(= 2 (count %))
      (deal)                    => #(= [26 26]       (map count %))
      (deal 3)                  => #(= [18 17 17]    (map count %))
      (deal 4)                  => #(= [13 13 13 13] (map count %))
      (set (flatten  (deal 3))) => #(= 52 (count %))
      )
    
(fact "When playing a round, the highest value card wins"
      (play-round #{:diamond 3} #{:diamond 8} )         => 2
      (play-round #{:diamond 5} #{:diamond 2} )         => 1
      (play-round #{:heart 5}   #{:diamond 5} )         => 1
      (play-round #{:diamond :queen} #{:diamond :jack}) => 1
      (play-round #{:spade :king} #{:spade :queen})     => 1
      (play-round #{:club :ace} #{:heart :king})        => 1
      (play-round #{:club 8} #{:spade 8})               => 1
      (play-round #{:diamond :ace} #{:club :ace})       => 1
      (play-round #{:heart :jack} #{:diamond :jack})    => 1)

(fact "the player loses when they run out of cards"
      (play-game [ #{:diamond 3} ]  [                ] ) => 1
      (play-game []                 [ #{:club :king} ] ) => 2
      (play-game [ #{:diamond 3} ]  [ #{:club :king} ] ) => 2
      (play-game (deal))                                 => #(some #{%} [1 2]))
