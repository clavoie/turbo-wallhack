;; this should probably be somewhere else

(ns turbo-wallhack.strings)

(def tokenize-re #"(?imu)[a-z0-9]+(-[a-z0-9]+)*('s?)?")

(def english-stop-words (sorted-set "a" "an" "and" "are" "as" "at" "be" "but" "by"
                                    "for" "if" "in" "into" "is" "it"
                                    "no" "not" "of" "on" "or" "such"
                                    "that" "the" "their" "then" "there" "these"
                                    "they" "this" "to" "was" "will" "with"))

(defn english-filter
  "This should probably be moved somewhere else"
  [str-value]
  (assert (string? str-value) "Value must be a string")
  (not (get english-stop-words str-value)))

(defn tokenize
  "Takes a string and returns a lazy sequence of tokens

  This should probably be somewhere else"
  [str-value]
  (assert (string? str-value) "Value must be a string")
  (map first (re-seq tokenize-re str-value)))
