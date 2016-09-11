(ns fox-goose-bag-of-corn.puzzle
  (:require [clojure.tools.logging :as log]))

(def start-pos [[#{:fox :goose :corn :you} #{:boat} #{} ]] )

; generate sets of items in each location
(defn lhbank [[loc _ _]] loc )
(defn boat   [[_ loc _]] loc )
(defn rhbank [[_ _ loc]] loc )
(defn cargo [things] (disj things :you :boat))


(defn moves [gstate] 
  (let [from             (fn [current items] (log/spy  (map  #(disj  current % :you) items)))
        to               (fn [current items] (log/spy  (map  #(conj  current % :you) items)))
        nop              (fn [current items] (log/spy  (map  (fn [_] current)        items))) 
        current_loc      (map :you gstate)
        [itemloc actions]  (case current_loc
                           [:you nil  nil ] [lhbank  '([from to nop])]
                           [nil  :you nil ] [boat    '([to from nop] [nop from to])]
                           [nil  nil  :you] [rhbank  '([nop to from])] )
        items            (cargo (itemloc gstate))
        getnew           (fn [operation current]  (log/spy :info (operation current items)))]
    ( map #(map getnew %1 gstate) actions )
    ))


(defn turn [gstate]
  ; find 'you'
  ( let [hops (cond 
    (some #{:you} (lhbank gstate)) [lhbank boat rhbank]
    (some #(:you) (rhbank gstate)) [rhbank boat lhbank])]
    hops)
  )

(defn river-crossing-plan []
  start-pos)
