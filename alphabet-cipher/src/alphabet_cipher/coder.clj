(ns alphabet-cipher.coder)

(def  baseseq (seq "abcdefghijklmnopqrstuvwxyz")) 

(defn expand [ keyword ] (cycle keyword))

(defn character_index [ inchar inseq ]
  (let [ searchchar (Character/toLowerCase inchar)]
    (first  (keep-indexed (fn [index x] (when (= x searchchar) index)) inseq))))

(defn row_seq [inchar]
    (let [ index   (character_index inchar baseseq)
           back     (take index baseseq) 
           front   (drop index baseseq) ] 
      (concat front back)))



(defn encode_kmpair [ [kchar mchar] ] 
  (nth (row_seq kchar) (character_index mchar baseseq)  ) )

(defn decode_kmpair [ [kchar mchar] ] 
  (nth baseseq (character_index mchar (row_seq kchar))  ) )

(defn decipher_cmpair [ [cchar mchar] ] 
  (nth baseseq (character_index cchar (row_seq mchar)) ) )


(defn make_km_pairs [ keyword message ]
  (partition 2 (interleave (expand keyword) message )))

(defn encode [keyword message]
  (let [ kmpairs (make_km_pairs keyword message) ]
    (apply str (map encode_kmpair kmpairs ))))

(defn decode [keyword message]
  (let [ kmpairs (make_km_pairs keyword message) ]
    (apply str (map decode_kmpair kmpairs ))))

(defn decipher_expanded [cipher message]
  (let [ cmpairs (make_km_pairs cipher message)]
    (map decipher_cmpair cmpairs )))

(defn minimise_keyword [expanded]
    (loop [ n 1 ]
      (let [ keyword (take n expanded) 
             totest  (make_km_pairs keyword expanded)]
      (if (every? (fn [[a b]] (= a b)) totest)
        (apply str keyword)
        (recur (inc n))))))  

(defn decipher [cipher message]
  (minimise_keyword ( decipher_expanded cipher message ) ))

