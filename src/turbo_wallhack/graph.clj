(ns turbo-wallhack.graph
  (:require [turbo-wallhack.node :as node]))

(defn create
  "Creates a new index graph"
  []
  {:root (node/make-root)})

;; private
(defn get-or-create
  "Returns a node for a token or creates it if needed, returning the new graph"
  [graph token]
  (let [token-seq (concat (seq token) (list :eos))]
    (loop [graph graph
           path [:root]
           seq-this (first token-seq)
           seq-rest (rest token-seq)]
      ())))

(defn add
  "Adds a token to the graph, returning the new graph"
  [graph token]
  (let [token-seq (concat (seq token) (list :eos))]))
