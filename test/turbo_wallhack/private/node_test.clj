(ns turbo-wallhack.private.node-test
  (:require [clojure.test :refer :all]
            [turbo-wallhack.private.node :refer :all]))

;;
;; helpers
;;

(defn assert-node-path
  "Asserts that a node:

  1) exists at a given path from the root node
  2) has the edge count expected of it
  3) contains a path matching the path to the node"
  [root path expected-edges]
  (let [node (get-in root path)
        edges (get node :edges)
        node-path (get node :path)]
    (is (= expected-edges (count edges)))
    (is (= node-path path))))

;;
;; tests
;;

(deftest create-test
  (testing "node created successfully"
    (let [token \a
          test-path [:a]
          node (create token test-path)
          {:keys [index edges path value]} node]
      (is (map? node))
      (is (= token index))
      (is (= {} edges))
      (is (= test-path path))
      (is (nil? value))))
  (testing "assertion error thrown if path is invalid"
    (is (thrown? AssertionError (create \a '())))))

(deftest ensure-path-test
  (let [node (create \a [])
        node (ensure-path node "lso")
        node (ensure-path node "lsi")
        node (ensure-path node "lwo")]
    (assert-node-path node [] 1)
    (assert-node-path node [:edges \l] 2)
    (assert-node-path node [:edges \l :edges \s] 2)
    (assert-node-path node [:edges \l :edges \w] 1)
    (assert-node-path node [:edges \l :edges \s :edges \o] 0)
    (assert-node-path node [:edges \l :edges \s :edges \i] 0)
    (assert-node-path node [:edges \l :edges \w :edges \o] 0)))

(deftest get-value-path-test
  (is (= [:edges \h :edges \e :edges \l :edges \l :edges \o :values] (get-value-path "hello"))))

(deftest children-test
  (let [node (create \a [])
        node (ensure-path node "lso")
        node (ensure-path node "lsi")
        childs (map #(get %1 :index) (children node))]
    (is (= '(\l \s \o \i)))))

