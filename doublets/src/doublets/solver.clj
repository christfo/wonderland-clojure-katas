(ns doublets.solver
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def words (-> "words.edn"
               (io/resource)
               (slurp)
               (read-string)))

(defn relatable [word] (let [n (count word)] (filter #(= n (count %)) words)))

(defn is-transition? [from to] 
  (if (= (count from) (count to))
    (let  [  differlist            (map not= from to)  
            [differs & remaining]  (drop-while false? differlist) ]
      (and differs (empty? (drop-while false? remaining))))))

(defn expand-node 
  ( [node]             ( expand-node (relatable (first node)) node) )
  ( [dictionary node] 
   (let [ transitions        (filter #(is-transition? % (first node)) dictionary)  
         valid_transitions  (filter #(not-any? #{%} node)            transitions) ]
     (map #(cons % node) valid_transitions) )))


(defn gen-next-layer 
  ( [nodes]            (gen-next-layer (relatable (first (first nodes))) nodes))
  ( [candidates nodes] (mapcat (partial expand-node candidates) nodes)))


(defn solution? [nodes destination] 
  (some #(when (= (first %) destination) %) nodes))


(defn doublets [word1 word2]
  (let [ candidates   (relatable word2) ]
    (loop [ nodes  (list [word2]) ] 
      (if  (not-empty nodes)
        (or  
          (solution? nodes word1)   
          (recur (gen-next-layer candidates nodes)))  )) ))

