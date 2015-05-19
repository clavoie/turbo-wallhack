(ns turbo-wallhack.node
  (:require [turbo-wallhack.private.node :as private]))

(defn make-root
  "Create a new root node for a new graph"
  []
  (private/create :root))

(defn add
  "Adds a token to the node index, returns the updated node"
  [node token value]
  (let [node (private/ensure-path node token)
        token-seq (seq token)
        path (private/get-value-path token)]
    (update-in node path #(conj %1 value))))
