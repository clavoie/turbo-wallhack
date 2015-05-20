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
        edges (get-edges node)
        node-path (get-path node)]
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

(deftest get-index-test
  (is (= \a (get-index (create \a [])))))

(deftest get-edges-test
  (is (= {} (get-edges (create \a [])))))

(deftest get-path-test
  (is (= [\a \b \c] (get-path (create \a [\a \b \c])))))

(deftest get-values-test
  (is (nil? (get-values (create \a [])))))

(deftest ensure-path-test
  (testing "empty token, no change"
    (let [node (create \a [])
          node (ensure-path node "hello")
          node2 (ensure-path node "")]
      (is (= node node2))))
  (testing "missing path created, existing paths added to"
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
      (assert-node-path node [:edges \l :edges \w :edges \o] 0))))

(deftest get-value-path-test
  (is (= [:edges \h :edges \e :edges \l :edges \l :edges \o :values] (get-value-path "hello"))))

(deftest get-children-test
  (let [node (create \a [])
        node (ensure-path node "lso")
        node (ensure-path node "lsi")
        childs (map #(get-index %1) (get-children node))]
    (is (= '(\l \s \o \i)))))

(deftest get-node-path-test
  (is (= [:edges \h :edges \e :edges \y] (get-node-path "hey"))))

(deftest get-value-path-test
  (is (= [:edges \h :edges \e :edges \y :values] (get-value-path "hey"))))

(deftest get-node-test
  (let [root (create :root [])
        root (ensure-path root "hey")
        valid-node (get-node root "he")
        invalid-node (get-node root "ho")]
    (is (map? valid-node))
    (is (= 1 (count (get-edges valid-node))))
    (is (= \e (get-index valid-node)))
    (is (nil? invalid-node))))

(deftest get-children-test
  (let [root (create :root [])
        root (ensure-path root "hey")
        root (ensure-path root "hello")
        root-children (get-children root)
        he-node (get-node root "he")
        he-children (get-children he-node)]
    (is (= 6 (count root-children)))
    (doseq [child root-children] (is (map? child)))
    (is (= 4 (count he-children)))
    (doseq [child he-children] (is (map? child)))))

(deftest leaf?-test
  (is (not (leaf? (create 1 []))))
  (is (leaf? (update-in (create 1 []) [:values] #(conj %1 1)))))

(deftest get-leaves-test
  (let [root (create :root [])
        root (ensure-path root "hey")
        root (update-in root (get-value-path "he") #(conj %1 1))
        leaves (get-leaves root)
        first-leaf (first leaves)]
    (is (= 1 (count leaves)))
    (is (map? first-leaf))
    (is (= \e (get-index first-leaf)))))

(deftest get-path-seq-test
  (let [root (create :root [])
        root (ensure-path root "hello")
        root (ensure-path root "hey")
        second-l-node (get-node root "hell")
        hey-node (get-node root "hey")]
    (is (= '(\h \e \l \l) (get-path-seq second-l-node)))
    (is (= '(\h \e \y) (get-path-seq hey-node)))))
