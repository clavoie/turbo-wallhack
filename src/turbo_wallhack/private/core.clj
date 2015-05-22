(ns turbo-wallhack.private.core
  (:require [turbo-wallhack.node :as node]))

(defn wallhack?
  "Tests is an object can be considered a wallhack

  wallhack - the object to test

  Returns true if the object can be considered a wallhack, and false otherwise"
  [wallhack]
  (let [{:keys [filter-fn tokenize-fn score-fn indexes weights]} wallhack]
    (and (fn? filter-fn)
         (fn? tokenize-fn)
         (fn? score-fn)
         (map? indexes)
         (map? weights))))

(defn get-filter-fn
  [wallhack]
  (get wallhack :filter-fn))

(defn get-tokenize-fn
  [wallhack]
  (get wallhack :tokenize-fn))

(defn ensure-indexes
  [wallhack value]
  (let [value-keys (keys value)
        default-indexes  (repeatedly (count value-keys) node/make-root)
        default-map (zipmap value-keys default-indexes)]
    (assoc wallhack :indexes (merge default-map (get wallhack :indexes)))))

(defn add-tokens
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
