(ns card-game-war.game)

;; feel free to use these cards or use your own data structure
(def suits [:spade :club :diamond :heart])
(def ranks [2 3 4 5 6 7 8 9 10 :jack :queen :king :ace])
(def cards
  (for [suit suits
        rank ranks]
    [suit rank]))

(defn deal 
  ( [players] (partition (quot (count cards) players) (shuffle cards)))
  ( []        (deal 2)))

(defn rank2value [rank]
  (first (keep-indexed (fn [index item] (when (= rank item) index)) ranks)))

(defn suit2value [suit]
  (first (keep-indexed (fn [index item] (when (= suit item) index)) suits)))

(defn play2value [[ suit rank]]
  (+ (* (count suits) (rank2value rank)) (suit2value suit)))

(defn play-round [ play1 play2 ]
  (if (> (play2value play1) (play2value play2)) 1 2))

(defn play-game 
  ( [ [hand1 hand2] ] (play-game hand1 hand2))
  ( [player1-cards player2-cards]
    (loop [ [first1 & rest1] player1-cards 
            [first2 & rest2] player2-cards ] 
      (or 
        (cond (not first1) 2 (not first2) 1) ; no cards in hand
        (case (play-round first1 first2)
          1 (recur (conj (vec rest1) first1 first2)    rest2) 
          2 (recur  rest1                             (conj (vec  rest2) first2 first1)) )))))


