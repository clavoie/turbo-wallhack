(ns turbo-wallhack.private.core-test
  (:require [clojure.test :refer :all]
            [turbo-wallhack.core :as core]
            [turbo-wallhack.node :as node]
            [turbo-wallhack.strings :as strings]
            [turbo-wallhack.private.core :refer :all]))

(deftest wallhack?-test
  (let [base-wallhack (core/create)
        missing-filter-fn (dissoc base-wallhack :filter-fn)
        missing-indexes (dissoc base-wallhack :indexes)
        missing-score-fn (dissoc base-wallhack :score-fn)
        missing-tokenize-fn (dissoc base-wallhack :tokenize-fn)
        missing-weights (dissoc base-wallhack :weights)
        additional-fields (assoc base-wallhack :a 1 :b 2)
        wrong-filter-fn (assoc base-wallhack :filter-fn 'a)
        wrong-indexes (assoc base-wallhack :indexes 'b)
        wrong-score-fn (assoc base-wallhack :score-fn 'c)
        wrong-tokenize-fn (assoc base-wallhack :tokenize-fn 'd)
        wrong-weights (assoc base-wallhack :weights 'e)]
    (is (wallhack? base-wallhack))
    (is (not (wallhack? [])))
    (is (not (wallhack? '())))
    (is (not (wallhack? {})))
    (is (not (wallhack? missing-filter-fn)))
    (is (not (wallhack? missing-indexes)))
    (is (not (wallhack? missing-score-fn)))
    (is (not (wallhack? missing-tokenize-fn)))
    (is (not (wallhack? missing-weights)))
    (is (wallhack? additional-fields))
    (is (not (wallhack? wrong-filter-fn)))
    (is (not (wallhack? wrong-indexes)))
    (is (not (wallhack? wrong-score-fn)))
    (is (not (wallhack? wrong-tokenize-fn)))
    (is (not (wallhack? wrong-weights)))))

(deftest get-filter-fn-test
  (let [test-fn (fn [])
        wallhack (core/create :filter-fn test-fn)]
    (is (= test-fn (get-filter-fn wallhack)))))

(deftest get-tokenize-fn-test
  (let [test-fn (fn [])
        wallhack (core/create :tokenize-fn test-fn)]
    (is (= test-fn (get-tokenize-fn wallhack)))))

(deftest ensure-indexes-test
  (let [wallhack (core/create)
        wallhack (assoc wallhack :indexes {:a 1 :b 2})
        wallhack (ensure-indexes wallhack {:a 3 :c 4 :d 5})
        node? #(map? %)]
    (is (wallhack? wallhack))
    (is (node? (get-in wallhack [:indexes :c])))
    (is (node? (get-in wallhack [:indexes :d])))
    (is (= 1 (get-in wallhack [:indexes :a])))
    (is (= 2 (get-in wallhack [:indexes :b])))))

(deftest add-tokens-test
  (let [wallhack (core/create)
        value {:aaa 1 :bbb 2}
        wallhack (ensure-indexes wallhack value)
        wallhack (add-tokens wallhack :aaa ["hey" "hello"] value)
        values (node/complete (get-in wallhack [:indexes :aaa]) "h")
        str-values (map #(apply str (get % :seq)) values)
        leaf-values (distinct (apply concat (map #(get % :values) values)))]
    (is (= 2 (count str-values)))
    (is (some #{"hey"} str-values))
    (is (some #{"hello"} str-values))
    (is (= 1 (count leaf-values)))
    (is (= value (first leaf-values)))))

(deftest add-to-indexes-test
  (let [wallhack (core/create :filter-fn strings/english-filter :tokenize-fn strings/tokenize)
        value {:a "hey there" :b "there hello at"}
        wallhack (ensure-indexes wallhack value)
        wallhack (add-to-indexes wallhack value)
        ah-values (node/complete (get-in wallhack [:indexes :a]) "h")
        at-values (node/complete (get-in wallhack [:indexes :a]) "t")
        bh-values (node/complete (get-in wallhack [:indexes :b]) "h")
        bt-values (node/complete (get-in wallhack [:indexes :b]) "t")
        ah-str-values (map #(apply str (get % :seq)) ah-values)
        bh-str-values (map #(apply str (get % :seq)) bh-values)
        leaf-values (distinct (concat (distinct (apply concat (map #(get % :values) ah-values)))
                                      (distinct (apply concat (map #(get % :values) bh-values)))))]
    (is (= '("hey") ah-str-values))
    (is (= '("hello") bh-str-values))
    (is (empty? at-values))
    (is (empty? bt-values))
    (is (= 1 (count leaf-values)))
    (is (= value (first leaf-values)))))
