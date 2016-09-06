(ns card-game-war.game-test
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
    (is (= 1 (play-round [:diamond 8] [:diamond 3]) )))
  (testing "queens are higher rank than jacks")
  (testing "kings are higher rank than queens")
  (testing "aces are higher rank than kings")
  (testing "if the ranks are equal, clubs beat spades")
  (testing "if the ranks are equal, diamonds beat clubs")
  (testing "if the ranks are equal, hearts beat diamonds"))

(deftest test-play-game
  (testing "the player loses when they run out of cards"))

