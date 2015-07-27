(ns urlshortener.core)

(defn int-to-mod-list [x m]
  (loop [n x
         digits '()]
    (if (<= n 0)
      digits
      (recur (int (/ n m))
             (conj digits (mod n m))))))

(def DIGIT_OFFSET 48)
(def LOWERCASE_OFFSET 61)
(def UPPERCASE_OFFSET 55)

(defn int-to-char [i]
  (char (cond
    (and (< i 10) (>= i 0)) (+ i DIGIT_OFFSET)
    (and (>= i 10) (<= i 35)) (+ i UPPERCASE_OFFSET)
    (and (>= i 36) (< i 62)) (+ i LOWERCASE_OFFSET)
    :else (throw Exception))))

(defn int-to-base62 [x]
  (clojure.string/join "" (map int-to-char (int-to-mod-list x 62))))