(ns wonderland-number.finder-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [wonderland-number.finder :refer :all]))

(defn hasAllTheSameDigits? [n1 n2]
  (let [s1 (set (str n1))
        s2 (set (str n2))]
    (= s1 s2)))

(deftest test-wonderland-number
  (testing "A wonderland number must have the following things true about it"
    (let [wondernum (wonderland-number)]
      (is (= 6 (count (str wondernum))))
      (is (hasAllTheSameDigits? wondernum (* 2 wondernum)))
      (is (hasAllTheSameDigits? wondernum (* 3 wondernum)))
      (is (hasAllTheSameDigits? wondernum (* 4 wondernum)))
      (is (hasAllTheSameDigits? wondernum (* 5 wondernum)))
      (is (hasAllTheSameDigits? wondernum (* 6 wondernum))))))

(facts "about match-digits"
       (fact "different numbers with uncommon digits fail to match"
             (match-digits? 1234 5678) => falsey )
       (fact "equal numbers have the same digits"
             (match-digits? 1234 1234) => truthy)       
       (fact "rearranged numbers have the same digits"
             (match-digits? 1234 4321) => truthy)       
       (fact "numbers of unequal length with common digits match"
             (match-digits? 1234 12344321) => truthy)       
       )

(facts "about wonderland-number"
       (fact "142857 is a wonderland number"     (wonderland? 142857) => truthy)
       (fact "the number is returned when found" (wonderland? 142857) => 142857)
       (fact "123456 is not wonderland"          (wonderland? 123456) => falsey)
       )
