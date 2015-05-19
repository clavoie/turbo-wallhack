(ns turbo-wallhack.private.node)

(defn create
  "Creates a new node in the radix tree"
  [index]
  {:index index :edges {} :values nil})

(defn ensure-path
  "Ensures a path exists or creates it"
  [node token]
  (let [token-seq (seq token)]
    (loop [root node
           this-token (first token-seq)
           rest-tokens (rest token-seq)
           path [:edges this-token]]
      (if-not this-token
        root
        (recur (if (get-in root path) root (update-in root path (create this-token)))
               (first rest-tokens)
               (rest rest-tokens)
               (conj path :edges (first rest-tokens)))))))

(defn get-value-path
  [token]
  (let [token-seq (seq token)
        edge-pieces (repeatedly #(identity :edges))
        path (interleave edge-pieces token-seq)
        path (take (* 2 (count token)) path)
        path (vec path)]
    (conj path :values)))
