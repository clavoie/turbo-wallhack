(ns turbo-wallhack.strings-test
  (:require [clojure.test :refer :all]
            [turbo-wallhack.strings :refer :all]))

(deftest english-filter-test
  (testing "throws with invalid argument"
    (is (thrown? AssertionError (english-filter 1))))
  (testing "invalid values are filtered"
    (is (= '("best" "day" "ever") (filter english-filter '("this" "was" "the" "best" "day" "ever"))))))

(deftest tokenize-test
  (testing "throws with invalid argument"
    (is (thrown? AssertionError (tokenize [1 2 3 4]))))
  (testing "string can be tokenized successfully"
    (is (= '("hello" "sir" "this-is" "Ben's" "cat" "FluffX99") (tokenize "hello sir this-is Ben's cat FluffX99")))))
