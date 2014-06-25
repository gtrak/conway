(defproject conway "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-server "0.3.1"]
                 [compojure "1.1.7"]
                 [enlive "1.1.5"]
                 [hickory "0.5.3"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [om "0.6.2"]
                 [sablono "0.2.17"]
                 [jayq "2.5.1"]
                 [weasel "0.2.0"]
                 [prismatic/schema "0.2.2"]
                 ]

  :profiles {:dev {:plugins [[com.cemerick/austin "0.1.5-SNAPSHOT"]]}}
  :source-paths ["src"]
  :plugins [[lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.0"]
            ]

  :cljsbuild {:builds [{;; The path to the top-level ClojureScript source directory:
                        :source-paths ["src"]
                        ;; The standard ClojureScript compiler options:
                        ;; (See the ClojureScript compiler documentation for details.)
                        :compiler {:output-to "cljs-app.js"  ; default: target/cljsbuild-main.js
                                   :optimizations :advanced
                                   :source-map "cljs-app.js.map"
                                   :pretty-print false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]
                                   }
                        }
                       ]
}

  )
