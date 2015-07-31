(ns urlshortener.core
  (:require [clojure.math.numeric-tower :as math]
            [taoensso.carmine :as car :refer (wcar)]))

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

(defmacro wcar* [& body] `(car/wcar {:pool {} :spec {}} ~@body))

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

(defn get-and-inc-id []
  (wcar* (car/incr "next-key")))

(defn persist-url [short-url long-url]
  (wcar* (car/hset (str "url:" short-url) "name" long-url)))

(defn retrieve-url [short-url]
  (:name (wcar* (car/hget (str "url:" short-url)))))

(defn shorten [long-url]
  (let [id (get-and-inc-id)
        short-url (dehydrate id)]
    (persist-url short-url long-url)
    short-url))