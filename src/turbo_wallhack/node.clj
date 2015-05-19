(ns turbo-wallhack.node)

;;
;; private
;;

(defn create
  "Creates a new node in the radix tree"
  [index]
  {:index index :edges {} :values nil})

(defn ensure-path
  "Ensures a path exists or creates it"
  [node token]
  (let [token-seq (seq tokens)]
    (loop [root node
           this-token (first token-seq)
           rest-tokens (rest token-seq)
           path [:edges this-token]]
      (if-not this-token
        root
        (recur (if (get-in root path) root (update-in root path (create this-token)))
               (first rest-tokens)
               (rest rest-tokens)
               (conj path :edges (first this-token)))))))

(defn get-value-path
  [token]
  (let [token-seq (seq token)
        edge-pieces (repeatedly #(identity :edges))
        path (interleave edge-pieces token-seq)
        path (take (* 2 (count token)) path)
        path (vec path)]
    (conj path :value)))

;;
;; public
;;

(defn make-root
  "Create a new root node for a new graph"
  []
  (create :root))

(defn add
  "Adds a token to the node index, returns the updated node"
  [node token value]
  (let [node (ensure-path node token)
        token-seq (seq token)
        path (get-value-path token)]
    (update-in node path #(conj %1 value))))