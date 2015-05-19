(ns turbo-wallhack.node)

(defn create
  "Creates a new node in the radix tree"
  [index]
  {:index index :edges {} :values nil})

(defn make-root
  "Create a new root node for a new graph"
  []
  (create :root))

(defn index
  "Returns the index for the current node"
  [node]
  (get node :index))

(defn eos?
  "Tests if the current node is an end of stream node. Returns true if it is, and false otherwise"
  [node]
  (= :eos (index node)))

(defn root?
  "Tests if the current node is root. Returning true if it is, and false otherwise"
  [node]
  (= :root (index node)))
