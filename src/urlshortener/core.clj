(ns urlshortener.core
  (:require [clojure.math.numeric-tower :as math]))

(defn long-to-mod-list [x m]
  (if (= 0 x)
    '(0)
  (loop [n x
         digits '()]
    (if (<= n 0)
      digits
      (recur (long (/ n m))
             (conj digits (mod n m)))))))

(def BASE 62)
(def DIGIT_OFFSET 48)
(def LOWERCASE_OFFSET 61)
(def UPPERCASE_OFFSET 55)

(defn is-digit? [c]
  (let [zero (long \0)
        nine (long \9)]
    (<= zero (long c ) nine)))

(defn char-to-long [c]
  (let [i (long c)]
    (cond
    (is-digit? c) (-  i DIGIT_OFFSET)
    (and (>= i (long \A)) (<= i (long \Z))) (- i UPPERCASE_OFFSET)
    (and (>= i (long \a)) (<= i (long \z))) (- i LOWERCASE_OFFSET)
    :else (throw Exception))))

(defn saturate [base]
  (loop [sum 0
         reversed-base (reverse base)
         exp 0]
    (if (nil? reversed-base)
      sum
      (recur (+ sum (* (char-to-long (first reversed-base)) (math/expt BASE exp))) (next reversed-base) (inc exp))
    )))

(defn long-to-char [i]
  (char (cond
    (and (< i 10) (>= i 0)) (+ i DIGIT_OFFSET)
    (and (>= i 10) (<= i 35)) (+ i UPPERCASE_OFFSET)
    (and (>= i 36) (< i 62)) (+ i LOWERCASE_OFFSET)
    :else (throw Exception))))

(defn dehydrate [x]
  (clojure.string/join "" (map long-to-char (long-to-mod-list x 62))))