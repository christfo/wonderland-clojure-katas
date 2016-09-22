(ns tiny-maze.solver)

(def tmaze [[:S 0 1]
            [1  0 1]
            [1  0 :E]] )

(defn right [[w h] loc] (let [newloc (inc loc)] (if-not (zero? (mod newloc w)) newloc)))
(defn left  [[w h] loc] (let [newloc (dec loc)] (if-not (zero? (mod loc w   )) newloc)))
(defn up    [[w h] loc] (let [newloc (+ loc w)] (if-not (>= newloc (* w h)) newloc)))
(defn down  [[w h] loc] (let [newloc (- loc w)] (if-not (neg? newloc) newloc)))
(defn start [maze] (first  (keep-indexed #(when (= :S %2) %1) maze)))
(defn end   [maze] (first  (keep-indexed #(when (= :E %2) %1) maze)))
(defn valid? [maze loc] (when (and loc (zero? (nth maze loc))) loc))
(defn take-step [maze loc] (concat (take loc maze) '(:x) (rest (nthrest maze loc ))))
(defn lockops [ ops [w h :as dims] ] (map #( partial % dims ) ops ) )
(defn solved? [end [loc & _ :as path]] (when  (= loc end) path))

(defn turn-choices [ maze ops [loc & previous :as path] ]
  (let [newlocs (keep #(valid? maze (% loc)) ops)]
    (remove (set path) newlocs)) ) ; don't turn back on yourself

(defn expand-path [ maze ops [loc & previous :as path] ]
  (let [nextlocs (turn-choices maze ops path)] 
    (for [nextloc nextlocs]  (cons nextloc path))))

(defn solve-path [[w h :as dims] maze]
  (let [ ops                  (lockops [right down left up] dims)
        initial-path          [ (start maze) ]
        solved?               (partial solved? (end maze))
        maze                  (replace {:S 0 :E 0} maze)  ]
    (loop [ paths [ initial-path ] ] 
      (let [ newpaths  (mapcat #(expand-path maze ops %) paths)
             solution  (some solved? newpaths) ]
        (or solution (recur newpaths))))))

(defn solve-maze [maze]
  (let [solution     (solve-path [(count (first maze)) (count maze)] (flatten maze))
        solved-maze  (reduce take-step (flatten maze) solution)]
    (partition (count (first maze)) solved-maze)))

