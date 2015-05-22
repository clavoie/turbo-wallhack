(ns turbo-wallhack.private.node)

(defn create
  "Creates a new node in the radix tree.

  index - the item in the sequence by which this node is indexed
  path - the path to this node in the radix tree

  Returns the new node"
  [index path]
  (assert (vector? path) "path must be a vector")
  {:index index :edges {} :path path :values nil})

(defn get-index
  "Returns the item in the sequence by which the node is indexed

  node - the node for which the index should be returned

  Returns the index for the given node"
  [node]
  (get node :index))

(defn get-edges
  "Returns the edges, child nodes, for this node. The edges will be a map of {node-index node}

  node - the node for which the edges should be returned

  Returns the edges for the given node"
  [node]
  (get node :edges))

(defn get-path
  "Returns the path used to reach this node. This path can be passed into (get-in), etc

  node - the node for which the path should be returned

  Returns the path for the given node. The path will be a vector"
  [node]
  (get node :path))

(defn get-values
  "Returns the values this node contains, if it is a leaf node

  node - the node for which the values should be returned

  Returns the sequence of values for this node, or nil if this node contains no values"
  [node]
  (get node :values))

(defn ensure-path
  "Ensures a path exists within a node, creating it if needed.

  root - the base node from which the path should be created
  value - the value which will be sequenced out from the root node. (seq value) is called to create the sequence

  Returns the updated root, or the existing root if no change was needed"
  [root value]
  (let [value-seq (seq value)]
    (loop [root root
           this-item (first value-seq)
           rest-items (rest value-seq)
           path [:edges this-item]]
      (if-not this-item
        root
        (recur (if (get-in root path) root (assoc-in root path (create this-item path)))
               (first rest-items)
               (rest rest-items)
               (conj path :edges (first rest-items)))))))

(defn get-node-path
  "Returns a path for a token which can be used to lookup a node in the radix tree

  value - the value which will be used to create the path. (seq value) is used to create the sequence

  Returns a vector which can be used to look up a node in the radix tree"
  [value]
  (let [value-seq (seq value)
        edge-seq (repeat :edges)
        path (interleave edge-seq value-seq)
        path (take (* 2 (count value)) path)]
    (vec path)))

(defn get-value-path
  "Returns the path for the values collection of a node

  value - the value which will be used to create the path. (seq value) is used to create the sequence

  Returns a vecor which can be used to look up a node's value"
  [value]
  (conj (get-node-path value) :values))

(defn get-node
  "Returns a node in the tree, or nil if no such node exists

  root - the root node from which the graph should be searched
  value - the value which will be used to search the tree. (seq value) will be used to create the search sequence

  Returns a node, or nil"
  [root value]
  (get-in root (get-node-path value)))

(defn get-children
  "Returns all the child and grandchild nodes of a root node

  root - the node whose children should be returned

  Returns a lazy sequence of all the child and grandchild nodes"
  [root]
  (let [child-nodes (for [[_ child] (get-edges root)] child)
        granchild-nodes (map get-children child-nodes)]
    (apply concat (conj granchild-nodes child-nodes))))

(defn leaf?
  "Tests if a node is a leaf node or not. A node is considered to be a leaf node if it has one or more values

  node - the node to test

  Returns a truthy value if the node is a leaf node, or nil if it is not"
  [node]
  (not-empty (get-values node)))

(defn get-leaves
  "Returns all the child nodes from a root node which are leaf nodes

  root - the node which should be searched for leaf nodes

  Returns a lazy sequence of leaf nodes based off of the root node"
  [root]
  (filter leaf? (get-children root)))

(defn get-path-seq
  "Returns the sequence of indexes which lead to node

  node - the node whose index path should be returned

  Returns a lazy sequence of index values which lead to the node in the radix tree"
  [node]
  (filter #(not= :edges %1) (get-path node)))
