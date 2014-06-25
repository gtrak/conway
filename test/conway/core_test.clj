(ns conway.core-test
  (:require [clojure.test :refer :all]
            [conway.core :as c]
            [clojure.pprint :as pp]))

(def cases
  (partition 2 [;; fewer than 2 dies
                [[false false false]
                 [false true false]
                 [false false false]]
                [[false false false]
                 [false false false]
                 [false false false]]
                ;; they just die
                [[true true false]
                 [false false false]
                 [false false false]]
                [[false false false]
                 [false false false]
                 [false false false]]
                ;; two or three lives on, middle flips on
                [[true true false]
                 [true false false]
                 [false false false]]
                [[true true false]
                 [true true false]
                 [false false false]]
                ;; more than 3 neighbors, dies
                [[true true true]
                 [true true false]
                 [false false false]]
                [[true false true]
                 [true false true]
                 [false false false]]
                ;; boundary, everything false
                [[false true false]
                 [true false true]
                 [false true false]]
                [[false false false]
                 [false false true]
                 [false false false]]]))

(defn print-grid
  [grid]
  (print "[")
  (doseq [row (butlast grid)]
    (println row))
  (println (str (last grid) "]")))


(deftest test-conway
  (doseq [[input output] cases]
    (println "Input:")
    (print-grid input)
    (println "Output:")
    (print-grid output)
    (is (= output
           (c/update input)))))
