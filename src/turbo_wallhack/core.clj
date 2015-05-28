(ns turbo-wallhack.core
  (:require [turbo-wallhack.private.core :as private]))

;;
;; create, => can predefine indexes with weights, give a predefined tokenizer, give predefined token filters, etc
;; (store wallhack map), each field in the map is given an index, each value of the map is
;;

(defn create
  "Creates a new turbo wallhack"
  [& {:keys [filter-fn tokenize-fn score-fn weights] :or {filter-fn identity tokenize-fn identity score-fn identity weights {:default 1}}}]
  ;; assert
  {:filter-fn filter-fn
   :indexes {}
   :score-fn score-fn
   :tokenize-fn tokenize-fn
   :weights weights})

;; wallhack?

(defn store
  [wallhack value]
  (assert (private/wallhack? wallhack) "invalid wallhack object")
  (assert (map? value "value must be a map"))
  (let [wallhack (private/ensure-indexes wallhack value)]
    (private/add-to-indexes wallhack value)))

(defn search
  [wallhack value]
  ;; assert wallhack
  ;; assert value seq
  )

