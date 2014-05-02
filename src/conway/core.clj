(ns conway.core)


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
  ;; boundary cases
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
  ;; rules

  
  )

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


;; square grid


