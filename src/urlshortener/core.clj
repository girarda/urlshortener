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

(defmacro wcar* [& body]
  `(car/wcar {:pool {} :spec {}} ~@body))

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

(defn set-number-visits [short-url visits]
  (wcar* (car/hset (str "url:" short-url) "visits" visits)))

(defn set-short-url [short-url long-url]
  (wcar* (car/hset (str "url:" short-url) "name" long-url))
  (wcar* (car/hset "urls" long-url short-url)))

(defn persist-url [short-url long-url]
  (dosync
    (set-number-visits short-url 1)
    (set-short-url short-url long-url)))

(defn register-url [long-url]
  (let [id (get-and-inc-id)
            short-url (dehydrate id)]
        (persist-url short-url long-url)
        short-url))

(defn retrieve-url [short-url]
  (wcar* (car/hget (str "url:" short-url) "name")))

(defn parse-int [s]
  (Integer/parseInt (re-find #"\A-?\d+" s)))

(defn update-url [short-url]
  (dosync
    (let [visits (wcar* (car/hget (str "url:" short-url) "visits"))]
      (set-number-visits short-url (inc (parse-int visits)))))
  short-url)

(defn retrieve-short-from-long-url [url]
  (wcar* (car/hget "urls" url)))

(defn shorten [long-url]
  (let [persisted-url (retrieve-short-from-long-url long-url)]
    (if (nil? persisted-url)
        (register-url long-url)
        (update-url persisted-url))))