(ns urlshortener.core-test
  (:require [clojure.test :refer :all]
            [urlshortener.core :refer :all]))

(deftest test-int-to-mod-list
  (testing "int-to-mod-list-returns-list-of-modulos"
    (is (= [2 1]
           (int-to-mod-list 125 62)))))

(deftest test-int-to-char
  (testing "single digit returns digit"
    (is (and (= \9
                (int-to-char 9))
             (= \0
                (int-to-char 0)))))
  (testing "between 10 and 35 return uppercase char"
        (is (and (= \A
                (int-to-char 10))
             (= \Z
                (int-to-char 35)))))
  (testing "between 36 and 61 return lowercase char"
        (is (and (= \a
                (int-to-char 36))
             (= \z
                (int-to-char 61)))))
  (testing "lower than 0 throws exception"
        (is (thrown? Exception
               (int-to-char -1))))
  (testing "higher than 61 throws exception"
        (is (thrown? Exception
               (int-to-char 62)))))

(deftest test-int-to-base62
  (testing "converts int to base62 string"
    (is (= "21"
           (int-to-base62 125)))))

(deftest test-char-to-int
  (testing "single digit returns digit"
    (is (and (= 0
                (char-to-int \0))
             (= 9
                (char-to-int \9)))))
  (testing "between 10 and 35 return uppercase char"
        (is (and (= 10
                (char-to-int \A))
             (= 35
                (char-to-int \Z)))))
  (testing "between 36 and 61 return lowercase char"
        (is (and (= 36
                (char-to-int \a))
             (= 61
                (char-to-int \z)))))
  (testing "higher than 61 throws exception"
        (is (thrown? Exception
               (char-to-int \tab)))))