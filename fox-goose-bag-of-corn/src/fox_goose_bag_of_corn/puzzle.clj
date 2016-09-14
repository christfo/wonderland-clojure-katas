(ns fox-goose-bag-of-corn.puzzle
  (:require [clojure.tools.logging :as log]))

(def start-pos [ #{:fox :goose :corn :you} #{:boat} #{} ] )
(def end-pos   [ #{} #{:boat} #{:you :fox :corn :goose} ] )

(defn solved? [path] (if (= end-pos (last path)) path))

; generate sets of items in each location
(defn lhbank [[loc _ _]] loc )
(defn boat   [[_ loc _]] loc )
(defn rhbank [[_ _ loc]] loc )
(defn cargo [things] (conj  (disj things :you :boat) nil))

; state transitions
(defn  from  [current item] (disj  current item :you ))
(defn  to    [current item] (disj (conj  current item :you) nil) )
(defn  nop   [current item]  current) 


(defn moves [gstate] 
  " generate the next layer of available states from the current one"
  (let [
        [itemloc actions]  (case (map :you gstate)
                             [:you nil  nil ] [lhbank  [ [from to nop]               ] ]
                             [nil  :you nil ] [boat    [ [to from nop] [nop from to] ] ]
                             [nil  nil  :you] [rhbank  [ [nop to from]               ] ] )]
    (for [ action  actions, item  (cargo (itemloc gstate)) ]
      (map #(%1 %2 item) action  gstate ))))


(defn legal [state] 
  "take a state and test its legality / if it is terminal"
  (let [ boat-capacity  #(> (count (cargo (boat %))) 2)   ; cargo has a nil which is counted 
         goose-death    #(some (fn [loc] (and (loc :goose) (loc :fox)     (not ( loc :you)))) % ) 
         corn-death     #(some (fn [loc] (and (loc :corn)    (loc :goose) (not ( loc :you)))) % ) 
         many-any       #(not= 5 (reduce + (map count %)))
         missing        #(not= #{:fox :goose :corn :you :boat} (reduce (fn [c i] (into c i)) #{} %))
         boat-moved     #(not= [nil :boat nil] (map :boat %)) ]
      (not ( (some-fn boat-capacity goose-death corn-death many-any missing boat-moved) state)) ))



(defn ppath [ paths ]
  "Game path pretty printer"
  (println "state ------------------------" )
  (doall (for [path paths] (do
        (println "    path ------------------------" )
        (doall (for [step path] (println "         - " step)))))))

(defn river-crossing-plan []
  "breadth first exhaustive search of each successive game state"
  (loop [ paths  [[start-pos]] ]
    (let [newpaths (reduce into [] 
                           (for [path paths] 
                             (let [newstates (moves (last path))
                                   legalstates (into [] (filter legal newstates)) ]
                               (for [state legalstates :when (not-any? #{state} path)] 
                                 (conj path state))  ))) ]
      (or (some solved? newpaths) (recur newpaths))))) 
