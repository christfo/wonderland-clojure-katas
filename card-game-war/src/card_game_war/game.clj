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

(defn play-round [ [suite1 rank1] [suite2 rank2] ]
  (when ()))

(defn play-game [player1-cards player2-cards])
