(ns alphabet-cipher.coder-test
  (:require [clojure.test :refer :all]
            [alphabet-cipher.coder :refer :all]))

(deftest test-expand
  (testing "that the keyword can be expanded"
    (is (= (seq  "vigilancevigilance")
           (take 18 (expand "vigilance"))))))

(deftest test-character_index 
  (testing "that characters may be converted to an index"
    (is ( = 0
            (character_index \a baseseq)))
    (is ( = 1
            (character_index \b baseseq)))
    (is ( = 25
            (character_index \z baseseq)))
    (is ( = 2
            (character_index \C baseseq)))
    (is ( = 24
            (character_index \Y baseseq)))
    ))

(deftest test-row_seq
  (testing "that we can generate the rotated input sequence")
  (is ( = (seq  "abcdefghijklmnopqrstuvwxyz")
          (row_seq \a)))
  (is ( = (seq  "efghijklmnopqrstuvwxyzabcd")
          (row_seq \e)))
  (is ( = (seq  "zabcdefghijklmnopqrstuvwxy")
          (row_seq \z)))
  (is ( = (seq  "abcdefghijklmnopqrstuvwxyz")
          (row_seq \A)))
  )

(deftest test-encode
  (testing "can encode given a secret keyword"
    (is (= "hmkbxebpxpmyllyrxiiqtoltfgzzv"
           (encode "vigilance" "meetmeontuesdayeveningatseven")))
    (is (= "egsgqwtahuiljgs"
           (encode "scones" "meetmebythetree")))))

(deftest test-decode
  (testing "can decode an cyrpted message given a secret keyword"
    (is (= "meetmeontuesdayeveningatseven"
           (decode "vigilance" "hmkbxebpxpmyllyrxiiqtoltfgzzv")))
    (is (= "meetmebythetree"
           (decode "scones" "egsgqwtahuiljgs")))))

(deftest test-decipher
  (testing "can extract the secret keyword given an encrypted message and the original message"
    (is (= "vigilance"
           (decipher "opkyfipmfmwcvqoklyhxywgeecpvhelzg" "thequickbrownfoxjumpsoveralazydog")))
    (is (= "scones"
           (decipher "hcqxqqtqljmlzhwiivgbsapaiwcenmyu" "packmyboxwithfivedozenliquorjugs")))))

