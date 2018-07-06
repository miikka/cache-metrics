(defproject miikka/cache-metrics "0.1.0"
  :description "Instrumentation for core.cache"
  :url "https://github.com/miikka/cache-metrics"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories [["releases" :clojars]]
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.cache "0.6.5"]
                 [io.dropwizard.metrics/metrics-core "3.2.2"]]
  :plugins [[lein-auto "0.1.3"]
            [lein-eftest "0.5.2"]])
