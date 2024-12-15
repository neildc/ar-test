(ns graph.core-test
  (:require [clojure.test :refer :all]
            [graph.core :refer :all]))

(deftest test-make-graph-with-seed
  (let [expected {1 [#graph.core.Edge{:node 5, :weight 8}],
                  2 [#graph.core.Edge{:node 4, :weight 6}],
                  3 [#graph.core.Edge{:node 4, :weight 8} #graph.core.Edge{:node 2, :weight 7}],
                  4 [#graph.core.Edge{:node 5, :weight 0} #graph.core.Edge{:node 4, :weight 2}],
                  5 []},
        actual (make-graph-with-seed 100 5 7)]

    (is (= expected actual))))

(def simple-graph
  {1 [{:node 2 :weight 1}]
   2 [{:node 3 :weight 1}]
   3 [{:node 4 :weight 1}]
   4 []})

(deftest test-shortest-path-simple
  (is (= (shortest-path simple-graph 1 4) [1 2 3 4])))

(deftest test-eccentricity-simple
  (is (= (eccentricity simple-graph 1) 3)))

(deftest test-radius-simple
  (is (= (radius simple-graph) 1)))

(deftest test-diameter-simple
  (is (= (diameter simple-graph) 3)))

(def bigger-graph
  {1 [{:node 2 :weight 1} {:node 3 :weight 1}]
   2 [{:node 4 :weight 2}]
   3 [{:node 4 :weight 3}]
   4 [{:node 5 :weight 3}]
   5 [{:node 6 :weight 2} {:node 7 :weight 1}]
   6 []
   7 []})

(deftest test-shortest-path-bigger
  (is (= (shortest-path bigger-graph 1 6) [1 2 4 5 6])))

(deftest test-eccentricity-bigger
  (is (= (eccentricity bigger-graph 4) 5)))

(deftest test-diameter-bigger
  (is (= (diameter bigger-graph) 8)))

(deftest test-radius-bigger
  (is (= (radius bigger-graph) 2)))
