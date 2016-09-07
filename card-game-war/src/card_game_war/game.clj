(ns card-game-war.game)

;; feel free to use these cards or use your own data structure
(def suits [:spade :club :diamond :heart])
(def ranks [2 3 4 5 6 7 8 9 10 :jack :queen :king :ace])

; map of card (as a set of suit and rank) to value
(def deck
  (into {} (map-indexed  (fn [index card] [card index])
        (for [rank ranks suit suits ] #{ suit rank } ))))

(defn deal 
  ( []        (deal 2))
  ( [players] (loop [ todeal  (shuffle (keys deck)) 
                      hands   [] 
                      round   players] 
                (if (> round 0) 
                  (recur (rest todeal) (conj hands (take-nth players todeal) ) (dec round))
                  hands))))

(defn play-round [ play1 play2 ]
  (if (> (deck play1) (deck play2)) 1 2))

(defn play-game 
  ( [ [hand1 hand2] ] (play-game hand1 hand2))
  ( [player1-cards player2-cards]
    (loop [ [first1 & rest1] player1-cards 
            [first2 & rest2] player2-cards ] 
      (or 
        (cond (not first1) 2 (not first2) 1) ; no cards in hand
        (case (play-round first1 first2)
          1 (recur (conj (vec rest1) first1 first2)  rest2) 
          2 (recur  rest1                           (conj (vec  rest2) first2 first1)) )))))


