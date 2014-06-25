(ns conway.dev-server
  (:require [ring.server.standalone :as server]
            [compojure.core :as c]
            [compojure.route :as route]
            [net.cgrand.enlive-html :as enlive]
            [weasel.repl.websocket :as weasel]
            [ring.util.response :as rr]
            [clojure.java.io :as io]))

(defn page []
  ((enlive/template
     (io/resource "public/index.html")
     []
     [:body] (enlive/append
              (enlive/html [:script (cemerick.austin.repls/browser-connected-repl-js)])))))

(c/defroutes handler
  (c/GET "/" [] (page))
  (route/resources "/" {:root "public"}))

(defn start
  []
  (server/serve #'handler {:open-browser true}))

;; (start)

;; Start up a repl

(def repl-env)

(defn init-cljs-repl []
  (->> (reset! cemerick.austin.repls/browser-repl-env
               (cemerick.austin/repl-env))
       constantly
       (alter-var-root #'repl-env))
  ;; return nil to avoid printing and OOM
  nil)

(defn cljs-repl
  "Turn your Clojure REPL into a ClojureScript REPL tied to that REPL environment"
  []
  (cemerick.austin/exec-env* repl-env ["google-chrome"])
  (cemerick.austin.repls/cljs-repl repl-env))

(defn weasel-repl
  []
  (cemerick.piggieback/cljs-repl
   :repl-env (weasel/repl-env
              :ip "0.0.0.0" :port 9001)))  

