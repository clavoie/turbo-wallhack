(ns turbo-wallhack.private.core
  (:require [turbo-wallhack.node :as node]))

(defn wallhack?
  "Tests is an object can be considered a wallhack

  wallhack - the object to test

  Returns a truethy value if the object can be considered a wallhack, and falsey value otherwise"
  [wallhack]
  (if (map? wallhack)
    (let [{:keys [filter-fn tokenize-fn score-fn indexes weights]} wallhack]
      (and (fn? filter-fn)
           (fn? tokenize-fn)
           (fn? score-fn)
           (map? indexes)
           (map? weights)))))

(defn get-filter-fn
  "Returns the filter function for the wallhack

  wallhack - the object which contains the filter function"
  [wallhack]
  (get wallhack :filter-fn))

(defn get-tokenize-fn
  "Returns the tokenize function for the wallhack

  wallhack - the object which contains the tokenize function"
  [wallhack]
  (get wallhack :tokenize-fn))

(defn ensure-indexes
  "Ensures that indexes exist for each of the values being added to a wallhack, before a value is stored in the wallhack

  wallhack - the wallhack in which the value will be stored
  value - a map which is the value to be stored in the wallhack

  Returns the updated wallhack with any missing indexes added. Existing indexes will not be altered"
  [wallhack value]
  (let [value-keys (keys value)
        default-indexes  (repeatedly (count value-keys) node/make-root)
        default-map (zipmap value-keys default-indexes)]
    (assoc wallhack :indexes (merge default-map (get wallhack :indexes)))))

(defn add-tokens
  "Adds a sequence of tokens to an index of the wallhack, storing the value in each token

  wallhack - the wallhack in which the value should be stored
  index - the name of the index in which the value should be stored
  tokens - a sequence of tokens which will be added to the index
  value - the value to associate with each token in the index

  Returns the updated wallhack object"
  [wallhack index tokens value]
  (loop [wallhack wallhack
         this-token (first tokens)
         rest-tokens (rest tokens)]
    (if-not this-token
      wallhack
      (let [path [:indexes index]
            node-o (get-in wallhack path)
            node-o (node/add node-o this-token value)]
        (recur (assoc-in wallhack path node-o)
               (first rest-tokens)
               (rest rest-tokens))))))

(defn add-to-indexes
  "Adds a value to a wallhack. Expects ensure-indexes to be called before this is called. The value of each key in
  the value map will be tokenized and filtered before it is added to its index

  wallhack - the wallhack to update
  value - a map containing the keys/values to be indexed

  Returns the updated wallhack object"
  [wallhack value]
  (let [filter-fn (get-filter-fn wallhack)
        tokenize-fn (get-tokenize-fn wallhack)
        value-keys (keys value)]
    (loop [wallhack wallhack
           this-key (first value-keys)
           rest-keys (rest value-keys)]
      (if-not this-key
        wallhack
        (let [tokens (tokenize-fn (get value this-key))
              filtered-tokens (filter filter-fn tokens)]
        (recur (add-tokens wallhack this-key filtered-tokens value)
               (first rest-keys)
               (rest rest-keys)))))))
