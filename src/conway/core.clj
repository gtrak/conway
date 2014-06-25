(ns conway.core)

(def offsets
  [[-1 -1] [0 -1] [1 -1]
   [-1 0]         [1 0]
   [-1 1]  [0 1]  [1 1]])

(defn neighboring-coords
  [[x y]]
  (for [[offset-x offset-y] offsets]
    [(+ x offset-x) (+ y offset-y)]))

(defn truthy-neighbors
  [grid [x y :as coord]]
  ;; -1 0 1 for xy , except [0,0]
  (let [max (dec (count grid))
        min 0
        neighboring (neighboring-coords coord)]
    (apply + (map (fn [[n-x n-y :as n-coord]]
                    (if (or (>= max n-y min) (>= max n-x min))
                        (if (get-in grid n-coord)
                          1
                          0)
                        0))
                  neighboring))))

(defn on?
  [truthy-neighbors current]
  (cond
   (> 2 truthy-neighbors) false
   ;; has to be above the 'current' rule, I think
   (= 3 truthy-neighbors) true
   ;; lives on if it's already alive
   (>= 3 truthy-neighbors 2) current
   (< 3 truthy-neighbors) false))

(defn gtrak-step
  [grid]
  (let [indices (range (count grid))
        coords (for [x indices
                     y indices]
                 [x y])]
    (reduce
     (fn [grid coord]
       (let [truthy (truthy-neighbors grid coord)
             current (get-in grid coord)]
         (assoc-in grid coord (on? truthy current))))
     grid
     coords)))

;; assumptions
;; square grid


;; cbp's code
;;--------------------------------------------------------------------

(defn neighbors
  [[x y]]
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :when (not= 0 dx dy)]
    [(+ dx x) (+ dy y)]))

(defn cbp-step
  [cells]
  (set (for [[loc n] (-> neighbors
                         (mapcat cells)
                         frequencies)
             :when (or (= n 3)
                       (and (= n 2) (cells loc)))]
         loc)))

(def glider #{[2 0] [2 1] [2 2] [1 2] [0 1]})

(defn set->grid
  "Rewrites a set GoL into a grid GoL"
  [cells]
  (let [xs    (map first cells)
        x_max (apply max xs)
        ys    (map second cells)
        y_max (apply max ys)
        d     (inc (max x_max y_max))
        grid  (vec (repeat d (vec (repeat d false))))]
    (reduce (fn [grid coord]
              (assoc-in grid coord true))
            grid cells)))

(defn make-grid
  "Makes an NxN grid, all false"
  [n]
  {:pre [(number? n)]}
  (vec (repeat n (vec (repeat n false)))))

(defn insert-grid
  "Inserts a grid in a parent at a given (x,y) offset
  from (0,0). Assumes that the child fits entirely within the parent."
  [dst [x y] src]
  {:pre [(number? x)
         (number? y)]}
  (-> (fn [dst [x₀ y₀]]
        {:pre [(vector? dst)
               (every? vector? dst)
               (number? x₀)
               (number? y₀)]}
        (assoc-in dst
                  [(+ x x₀) (+ y y₀)]
                  (get-in src [x₀ y])))
      (reduce dst
              (let [r (range (count src))]
                (for [x r
                      y r]
                  [x y])))))
