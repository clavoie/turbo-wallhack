(ns turbo-wallhack.private.node)

(defn create
  "Creates a new node in the radix tree

  index - the item in the sequence by which this node is indexed
  path - the path to this node in the radix tree"
  [index path]
  (assert (vector? path) "path must be a vector")
  {:index index :edges {} :path path :values nil})

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
        (recur (if (get-in root path) root (assoc-in root path (create this-token path)))
               (first rest-tokens)
               (rest rest-tokens)
               (conj path :edges (first rest-tokens)))))))

(defn get-node-path
  [token]
  (let [token-seq (seq token)
        edge-pieces (repeatedly #(identity :edges))
        path (interleave edge-pieces token-seq)
        path (take (* 2 (count token)) path)]
    (vec path)))

(defn get-value-path
  [token]
  (conj (get-node-path token) :values))

(defn get-edges-path
  [token]
  (conj (get-node-path token) :edges))

;; get child nodes => returns a lazy seq of all child nodes in a node

(defn children
  [node]
  (let [child-nodes (for [[_ child] (get node :edges)] child)
        granchild-nodes (map children child-nodes)]
    (apply concat (conj granchild-nodes child-nodes))))

(defn get-leaves
  [node]
  (filter #(not (empty? (get %1 :values))) (children node)))

(defn get-path-seq
  [node]
  (filter #(not= :edges %1) (get node :path)))

;; defn leaves => returns a lazy seq of all nodes with a non empty value...or with no children
