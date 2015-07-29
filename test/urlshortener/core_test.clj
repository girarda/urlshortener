(ns urlshortener.core-test
  (:require [clojure.test :refer :all]
            [urlshortener.core :refer :all]))

(deftest test-long-to-mod-list
  (testing "long-to-mod-list-returns-list-of-modulos"
    (is (= [2 1]
           (long-to-mod-list 125 62))))
  (testing "0 returns a list containing only 0"
    (is (= '(0)
           (long-to-mod-list 0 62)))))

(deftest test-long-to-char
  (testing "single digit returns digit"
    (is (and (= \9
                (long-to-char 9))
             (= \0
                (long-to-char 0)))))
  (testing "between 10 and 35 return uppercase char"
        (is (and (= \A
                (long-to-char 10))
             (= \Z
                (long-to-char 35)))))
  (testing "between 36 and 61 return lowercase char"
        (is (and (= \a
                (long-to-char 36))
             (= \z
                (long-to-char 61)))))
  (testing "lower than 0 throws exception"
        (is (thrown? Exception
               (long-to-char -1))))
  (testing "higher than 61 throws exception"
        (is (thrown? Exception
               (long-to-char 62)))))

(deftest test-dehydrate
  (testing "converts long to base62 string"
    (is (= "21"
           (dehydrate 125))))
  (testing "dehydrate returns right value"
    (is (= "123456789"
           (dehydrate 225557475374453)))))

(deftest test-char-to-long
  (testing "single digit returns digit"
    (is (and (= 0
                (char-to-long \0))
             (= 9
                (char-to-long \9)))))
  (testing "between 10 and 35 return uppercase char"
        (is (and (= 10
                (char-to-long \A))
             (= 35
                (char-to-long \Z)))))
  (testing "between 36 and 61 return lowercase char"
        (is (and (= 36
                (char-to-long \a))
             (= 61
                (char-to-long \z)))))
  (testing "higher than 61 throws exception"
        (is (thrown? Exception
               (char-to-long \tab)))))

(deftest test-saturate
  (testing "saturate returns right value"
    (is (= 225557475374453
           (saturate "0123456789")))))
