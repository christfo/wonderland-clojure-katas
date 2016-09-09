(ns doublets.solver-test
    (:use midje.sweet)
    (:require [clojure.test :refer :all]
              [doublets.solver :refer :all]))

(facts "about is-transition? test"
       (fact "a transition exists"
             (is-transition? "head" "heal") => truthy
             (is-transition? "book" "look") => truthy 
             (is-transition? "...." "....") => falsey 
             (is-transition? "aa.." "....") => falsey 
             (is-transition? ".aa." "....") => falsey 
             (is-transition? "..aa" "....") => falsey 
             (is-transition? "a.a." "....") => falsey 
             (is-transition? "...." "....") => falsey 
             (is-transition? "a"    "b")    => truthy
             (is-transition? "of"   "on")   => truthy
             (is-transition? "a..." "....") => truthy 
             (is-transition? ".a.." "....") => truthy 
             (is-transition? "..a." "....") => truthy 
             (is-transition? "...a" "....") => truthy )
       (fact "a transition does not exist"
             (is-transition? "head" "teal") => falsey
             (is-transition? "bank" "cbok") => falsey )
       (fact "a transition does not exist when the words are identical"
             (is-transition? "head" "head") => falsey )
       (fact "a transition does not exist when the first subword is a transition"
             (is-transition? "bead" "heady") => falsey )
       (fact "a transition does not exist when the second subword is a transition"
             (is-transition? "heady" "bead") => falsey )
       )

(facts "about node expansion"
       (fact "expand a single relation"
             (expand-node ["head"]) => [ ["heal" "head"] ])
       (fact "expand a dual relation"
             (expand-node ["heal"]) => (just [ ["teal" "heal"] ["head" "heal"] ] :in-any-order)) 
       (fact "expand a zero relation"
             (expand-node ["heart"]) => []) 
       (fact "expand a multi multi relation and ensure no loops"
             (expand-node ["heal" "head"]) => [ ["teal" "heal" "head"] ] )
       (fact "check a terminating chain"
             (expand-node ["head" "heal" "teal" "tell" "tall" "tail"]) => []) 
       )

(facts "about getting the next layer"
       (fact "generate a single relation"
             (gen-next-layer '( ["head"] ) ) => [ ["heal" "head"] ])
       (fact "generate a multi relation"
             (gen-next-layer '(["head"] ["heal"]) ) => (just [ ["heal" "head"] ["teal" "heal"] ["head" "heal"] ] :in-any-order))
       (fact "generate a multi relation"
             (gen-next-layer '(["heal" "head"] ["heal"]) ) => (just [ ["teal" "heal" "head"] ["teal" "heal"] ["head" "heal"] ] :in-any-order))
       )

(facts "about finding a solution"
       (fact "these chains have a solution"
             (solution? '( ["heal" "head"] ["tail" "mail"] )  "heal") => ["heal" "head"]
             (solution? '( ["tail" "mail"] ["heal" "head"] )  "heal") => ["heal" "head"]
             )
       (fact "these chains have no solution"
             (solution? '( ["heal" "head"] ["tail" "mail"] )  "fail") => falsey
             (solution? '( ["tail" "mail"] ["heal" "head"] )  "bob")  => falsey
             )
       (fact "these chains have two solutions, but the first is interesting"
             (solution? '( ["heal" "head"] ["tail" "mail"] ["heal" "meal"] )  "heal") => ["heal" "head"]
             (solution? '( ["tail" "mail"] ["heal" "head"] ["heal" "head"] )  "heal") => ["heal" "head"]
             )
       )

(facts "about solution"
       (fact "with word links found"
             (doublets "head" "tail")   => ["head" "heal" "teal" "tell" "tall" "tail"] 
             (doublets "door" "lock")   => ["door" "boor" "book" "look" "lock"]
             (doublets "bank" "loan")   => ["bank" "bonk" "book" "look" "loon" "loan"]
             (doublets "wheat" "bread") => ["wheat" "cheat" "cheap" "cheep" "creep" "creed" "breed" "bread"] )

       (fact "with no word links found"
             (doublets "ye" "freezer") => falsey )
       )
