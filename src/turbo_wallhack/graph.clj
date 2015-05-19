(ns turbo-wallhack.graph
  (:require [turbo-wallhack.node :as node]))

(defn create
  "Creates a new index graph"
  []
  {:root (node/make-root)})

(defn add
  "Adds a token to the graph, returning the new graph"
  [graph token]
  (let [token-seq (seq token)]))
