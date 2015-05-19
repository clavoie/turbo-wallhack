(ns turbo-wallhack.core)

(defn create
  "Creates a new turbo wallhack"
  []
  (sorted-map))

;; (add index "hey" {pod})

;; need to tokenize

;; ()

(defn add-value [index items value]
  (let [items (seq items)]
    (loop [index index
           vindexes (range 0 (count items))]
      (if (empty? items)
        index
        (recur (merge index )
               (rest vindexes))))))


(defn add-top-keys [index items]
  (loop [index index
         items (seq items)]
    (if (empty? items)
      index
      (recur (merge {(first items) []} index)
             (rest items)))))

(defn add
  "Adds an item to a search term in the wallhack"
  [index items value]
  (let [index (add-top-keys index items)
        ;; index (add-value items value)
        ]
    index))
