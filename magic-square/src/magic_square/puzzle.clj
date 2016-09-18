(ns magic-square.puzzle
  (:require [ clojure.tools.logging      :as log]))

(def values [1.0 1.5 2.0 2.5 3.0 3.5 4.0 4.5 5.0])

(defn pin-value [cv vset sum] 
  "given the centre value and the remaining values, group them into 4 sets that hit the sum"
  ; (println "  - " cv " : " vset)
  (let [psum (- sum cv)]
    (loop [coll []  vset vset]
      (if (empty? vset) ; All values are used
        [cv coll]
        (let [ v1 (first vset) v2 (- psum v1) ]
          ; (println "     - coll " coll ", " v1 " ? " v2 " : " vset)
          (if ( and (not= v1 v2) (vset v2) ) 
            (recur (conj coll #{v1 v2}) (disj vset v1 v2))
            ))))))

(defn partition-with-value [sum vset]
  ( filter some? (for [centre vset] (pin-value  centre (disj (set values) centre) sum)) ))

(defn order-diagonals [sum cv diagset]
  "diagonals - diagset is a list of pairs that form the horizontal, vertical and diagonal sums"
  (filter not-empty (for [p1p9 diagset] 
    ; use symmetry. it doesn't matter which value we try
    (let [p2p4p6p8-sets (disj diagset p1p9)] 
      (filter some? (for [p3p7 p2p4p6p8-sets]
        ; we have the corner values. Do the required values come from the remainder
        (let [solp2 (- sum (first p1p9)  (first p3p7)  )
              solp8 (- sum (second p1p9) (second p3p7) )
              solp4 (- sum (first p1p9)  (second p3p7) )
              solp6 (- sum (second p1p9) (first p3p7)  )
              solv  #{ solp2 solp8}
              solh  #{ solp4 solp6} ]
            (if (= (disj  p2p4p6p8-sets p3p7) #{solv solh})
              ; We have a solution. pack it up (symmetric solutions ignored)
              [ [ (first  p1p9)   solp2  (first p3p7)  ]
                [     solp4        cv       solp6      ]
                [ (second p3p7)   solp8  (second p1p9) ] ] 
              ))))) )))

(defn magic-square-solutions [values]
  (apply concat 
         (let  [n  3,  sum  (/ (reduce + values) n),  vset  (set values)]  
           ; try all values as a centre value
           (let [centre-solutions (partition-with-value sum vset)]
             (apply concat  (for [[cv vset] centre-solutions] 
                              ; and test candidate pairs as diagonals
                              (let [sols (order-diagonals sum cv (set vset))] 
                                sols)
                              ))))))

(defn magic-square [values]
  (first (magic-square-solutions values)))


