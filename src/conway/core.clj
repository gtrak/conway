(ns conway.core)

(def offsets
  [[-1 -1] [0 -1] [1 -1]
   [-1 0]         [1 0]
   [-1 1] [0 1] [1 1]])

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

(defn update [grid]
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


