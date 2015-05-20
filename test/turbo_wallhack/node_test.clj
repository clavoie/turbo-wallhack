(ns turbo-wallhack.node-test
  (:require [clojure.test :refer :all]
            [turbo-wallhack.node :refer :all]))

(deftest make-root-test
  (let [root (make-root)]
    (is (map? root))
    (is (= :root (get root :index)))))

(deftest add-test
  (let [root (make-root)
        root (add root "abc" 1)
        root (add root "abc" 2)
        root (add root "abd" 3)]
    (is (empty? (get-in root [:edges \a :values])))
    (is (empty? (get-in root [:edges \a :edges \b :values])))
                                ;; private/get-value-path?
    (is (= '(2 1) (get-in root [:edges \a :edges \b :edges \c :values])))
    (is (= '(3) (get-in root [:edges \a :edges \b :edges \d :values])))))

(deftest complete-test
  (let [root (make-root)
        root (add root "abc" 1)
        root (add root "abc" 2)
        root (add root "abd" 3)
        root (add root "abcd" 4)]
    (println (complete root "a"))))
