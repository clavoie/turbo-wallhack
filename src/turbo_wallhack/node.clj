(ns turbo-wallhack.node
  (:require [turbo-wallhack.private.node :as private]))

(defn make-root
  "Create a new root node for a new graph"
  []
  (private/create :root []))

(defn add
  "Adds a token to the node index, returns the updated node"
  [node token value]
  (let [node (private/ensure-path node token)
        path (private/get-value-path token)]
    (update-in node path #(conj %1 value))))

;; https://github.com/Yomguithereal/clj-fuzzy
;; rough cloud
;; word length score
;; number of values score
;; word distance score / multiplier
;; does index matter?
;; (store search-obj "text to index" doc)
;; (store search-obj :index-name "text to index" doc)
;; (search search-obj "thing") => ({:score score, matches: ({context: "matching text", :range-inclusive [5,10]}), :doc doc})


;; (complete root "hel") => (["help" score? (values)] ["hello" score? (values)])

(defn complete
  "Completes the sequence given a token"
  [root value]
  (if-let [node (private/get-node root value)]
    (for [leaf (private/get-leaves node)]
      {:seq (private/get-path-seq leaf) :values (private/get-values leaf)})))
