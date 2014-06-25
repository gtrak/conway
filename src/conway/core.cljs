(ns conway.core
  (:require [om.core :as om :include-macros true]
            [weasel.repl :as ws-repl]
            [om.dom :as dom :include-macros true]
            [clojure.browser.dom :as cljs-dom]
            [clojure.browser.net :as net]
            [clojure.core.reducers :as r]
            [cljs.core.async :as async :refer [<! >! put!]]
            [sablono.core :as html :refer-macros [html]]
            [cljs.reader :as reader])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))


(def offsets
  [[-1 -1] [0 -1] [1 -1]
   [-1 0]         [1 0]
   [-1 1] [0 1] [1 1]])

(def neighboring-coords
  (memoize (fn [[x y]]
             (for [[offset-x offset-y] offsets]
               [(+ x offset-x) (+ y offset-y)]))))

(defn truthy-neighbors
  [grid [x y :as coord]]
  ;; -1 0 1 for xy , except [0,0]
  (let [max (dec (count grid))
        min 0
        neighboring (neighboring-coords coord)]
    (->> neighboring
         (r/filter (fn [[n-x n-y]]
                     (and (>= max n-y min) (>= max n-x min))))
         (r/map (fn [n-coord]
                  (get-in grid n-coord)))
         (r/reduce (fn [ct val]
                     (if val
                       (inc ct)
                       ct))
                   0))))

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
    (r/reduce
     (fn [grid coord]
       (let [truthy (truthy-neighbors grid coord)
             current (get-in grid coord)]
         (update-in grid coord #(on? truthy %))))
     grid
     coords)))


(defn ^:export connect-repl
  []
  (println "Connecting to weasel REPL")
  (ws-repl/connect "ws://localhost:9001" :verbose true)
  (println "Connected"))

(defn column
  [val owner]
  (om/component
   (html [:td {:class (if val "on" "")}])))

(defn row
  [row owner]
  (om/component
   (html [:tr (om/build-all column row)])))

(defn main [data owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (let [comm (async/chan)]
        (om/set-state! owner :comm comm)))
    om/IRender
    (render [_]
      (html [:table.pure-table
             [:thead]
             [:tbody
              (om/build-all row data)]]))))


(defn rand-seq
  []
  (map (partial > 5) (repeatedly #(rand-int 10))))

(def app-state (atom (->> (conj (take 38 (repeat (take 40 (repeat 0))))
                                (take 40 (rand-seq))
                                (take 40 (rand-seq)))
                          shuffle
                          (mapv vec))))

(defn ^:export init
  []
  (om/root main app-state
           {:target (.getElementById js/document "root")})
  (go-loop [t (async/timeout 20)]
    (<! t)
    (swap! app-state update)
  (recur (async/timeout 1)))
  )
 
