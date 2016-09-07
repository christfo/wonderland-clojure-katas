(ns card-game-war.game-test 
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [card-game-war.game :refer :all]))


;; fill in  tests for your game
(deftest test-rank2value
  (testing "rank value"  (is (= 0   (rank2value 2))))
  (testing "rank value"  (is (= 6   (rank2value 8))))
  (testing "jack value"  (is (= 9   (rank2value :jack))))
  (testing "queen value" (is (= 10  (rank2value :queen))))
  (testing "king value"  (is (= 11  (rank2value :king))))
  (testing "ace value"   (is (= 12  (rank2value :ace))))
  (testing "bad value"   (is (= nil (rank2value :spade))))
  (testing "bad value"   (is (= nil (rank2value 1))))
  (testing "bad value"   (is (= nil (rank2value 11))))
  )

(deftest test-suit2value
  (testing "spade value"   (is (= 0   (suit2value :spade))))
  (testing "club value"    (is (= 1   (suit2value :club))))
  (testing "diamond value" (is (= 2   (suit2value :diamond))))
  (testing "heart value"   (is (= 3   (suit2value :heart))))
  (testing "bad value"     (is (= nil (suit2value :ace))))
  (testing "bad value"     (is (= nil (suit2value 5))))
  )

(deftest test-play-round
  (testing "dealing the cards"
    (is (count (deal 1)) 1)
    (is (count (deal 2)) 2)
    (is (count (deal 3)) 3)
    (is (count (deal)) 2))
    
  (testing "the highest rank wins the cards in the round"
    (is (= 1 (play-round [:diamond 8] [:diamond 3]) ))
    (is (= 2 (play-round [:diamond 3] [:diamond 8]) )))
  (testing "queens are higher rank than jacks"
    (is (= 1 (play-round [:diamond :queen] [:diamond :jack]) )))
  (testing "kings are higher rank than queens"
    (is (= 1 (play-round [:spade :king] [:spade :queen]) )))
  (testing "aces are higher rank than kings"
    (is (= 1 (play-round [:club :ace] [:heart :king]) )))
  (testing "if the ranks are equal, clubs beat spades"
    (is (= 1 (play-round [:club 8] [:spade 8]) )))
  (testing "if the ranks are equal, diamonds beat clubs"
    (is (= 1 (play-round [:diamond :ace] [:club :ace]) )))
  (testing "if the ranks are equal, hearts beat diamonds"
    (is (= 1 (play-round [:heart :jack] [:diamond :jack]) ))))

(deftest test-play-game
  (testing "the player loses when they run out of cards"
    (is (= 1 (play-game [ [:diamond 3] ]    [] )))
    (is (= 2 (play-game []                  [ [:club :king] ]  )))
    (is (= 2 (play-game [ [:diamond 3] ]    [ [:club :king] ]  )))
    (is (#{1 2} (play-game (deal))))
    ))


; try out facts...
(fact "the player loses when they run out of cards"
      (play-game [ [:diamond 3] ]   [])                  => 1
      (play-game []                 [ [:club :king] ]  ) => 2
      (play-game [ [:diamond 3] ]   [ [:club :king] ]  ) => 2
      (play-game (deal))                                 => #(some #{%} [1 2]))
