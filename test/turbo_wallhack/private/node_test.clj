(ns turbo-wallhack.private.node-test
  (:require [clojure.test :refer :all]
            [turbo-wallhack.private.node :refer :all]))

(deftest create-test
  (let [token \a
        node (create token)
        {:keys [index edges value]} node]
    (is (map? node))
    (is (= token index))
    (is (= {} edges))
    (is (nil? value))))

(deftest ensure-path-test
  (let [node (create \a)
        node (ensure-path node "lso")
        node (ensure-path node "lsi")
        node (ensure-path node "lwo")]
    (is (= 1 (count (get-in node [:edges]))))
    (is (= 2 (count (get-in node [:edges \l :edges]))))
    (is (= 2 (count (get-in node [:edges \l :edges \s :edges]))))
    (is (= 1 (count (get-in node [:edges \l :edges \w :edges]))))
    (is (= 0 (count (get-in node [:edges \l :edges \s :edges \o :edges]))))
    (is (= 0 (count (get-in node [:edges \l :edges \s :edges \i :edges]))))
    (is (= 0 (count (get-in node [:edges \l :edges \w :edges \o :edges]))))))

(deftest get-value-path-test
  (is (= [:edges \h :edges \e :edges \l :edges \l :edges \o :values] (get-value-path "hello"))))
