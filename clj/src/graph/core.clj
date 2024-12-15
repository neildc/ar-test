(ns graph.core
  (:require [clojure.data.priority-map :refer [priority-map]]))

(defrecord Edge [node weight])

(declare make-graph-with-seed)

(defn make-graph [N S]
  (make-graph-with-seed (rand-int Integer/MAX_VALUE) N S))

(defn make-graph-with-seed
  [seed n s]
  (let [max-weight 10
        random-gen (java.util.Random. seed)

        ;; Generate a random number using random-gen
        random-int (fn [upper-bound]
                     (.nextInt random-gen upper-bound))

        ;; Select a random element from a collection using random-gen
        random-element (fn [coll]
                         (nth coll (random-int (count coll))))]

    (when (or (< s (dec n)) (> s (* n (dec n))))
      (throw (IllegalArgumentException.
              (str "S must be between N-1 and N(N-1) inclusive\n"
                   "N: " n "\nS: " s "\nN-1: " (dec n)
                   "\nN(N-1): " (* n (dec n))))))

    (let [initial-connected-graph
          (reduce (fn [acc node]
                    (assoc acc node
                           [(Edge. (random-element (keys acc)) (random-int max-weight))]))
                  (sorted-map n [])
                  (reverse (range 1 n)))

          add-random-edge
          (fn [acc]
            (let [random-node (random-element (keys acc))
                  random-edge (loop [edge (random-element (keys acc))]
                                (if (some #(= (:node %) edge) (acc random-node))
                                  (recur (random-element (keys acc)))
                                  edge))]
              (update acc random-node conj (Edge. random-edge (random-int max-weight)))))]

      ;; Add edges until the desired number of edges is reached
      (reduce (fn [acc _] (add-random-edge acc))
              initial-connected-graph
              (range (inc (count (apply concat (vals initial-connected-graph)))) s)))))

(declare shortest-path-recur, get-path)

(defn shortest-path
  [graph start end]
  (let [distances (reduce (fn [acc node]
                            (assoc acc node (if (= node start) 0 Integer/MAX_VALUE)))
                          {}
                          (keys graph))
        prev (reduce (fn [acc node]
                       (assoc acc node nil))
                     {}
                     (keys graph))]
    (shortest-path-recur graph prev (priority-map start 0) distances start end)))

(defn shortest-path-recur
  [graph prev queue distances start end]
  (if (empty? queue)
    []
    (let [[current-node _] (peek queue)
          new-queue (pop queue)]
      (if (= current-node end)
        (get-path prev start end)
        (let [current-distance (distances current-node)
              updated (reduce (fn [acc {:keys [node weight]}]
                                (let [new-distance (+ current-distance weight)]
                                  (if (< new-distance (get distances node Integer/MAX_VALUE))
                                    {:distances (assoc (:distances acc) node new-distance)
                                     :queue (assoc (:queue acc) node new-distance)
                                     :prev (assoc (:prev acc) node current-node)}
                                    acc)))
                              {:distances distances :queue new-queue :prev prev}
                              (sort-by #(distances (:node %)) (graph current-node)))]
          (shortest-path-recur graph
                               (:prev updated)
                               (:queue updated)
                               (:distances updated)
                               start
                               end))))))

(defn get-path
  [prev start end]
  (if (= start end)
    [start]
    (conj (get-path prev start (prev end)) end)))

(defn eccentricity [graph node]
  (reduce
   (fn [acc other-node]
     (let [path (shortest-path graph node other-node)
           sum (reduce
                (fn [acc [from to]]
                  (+ acc (-> (first (filter #(= (:node %) to) (get graph from)))
                             :weight)))
                0
                (partition 2 1 path))]
       (max acc sum)))
   0
   (filter #(not= % node) (keys graph))))

(defn radius
  [graph]
  (reduce
   (fn [acc node]
     (let [e (eccentricity graph node)]
       (if (zero? e) acc (min acc e))))
   Integer/MAX_VALUE
   (keys graph)))

(defn diameter
  [graph]
  (reduce
   (fn [acc node]
     (max acc (eccentricity graph node)))
   0
   (keys graph)))

(defn -main
  [& args]

  (let [random-graph (make-graph 10 10)]
    (println random-graph)
    (shortest-path random-graph (first (keys random-graph)) (last (keys random-graph)))))

(defn debug
  [seed n s]
  (let [graph (make-graph-with-seed seed n s)]
    (println graph)
    (shortest-path graph (first (keys graph)) (last (keys graph)))))
