(defproject cache-metrics "0.1.3-SNAPSHOT"
  :description "Instrumentation for core.cache"
  :url "https://github.com/miikka/cache-metrics"
  :scm {:name "git", :url "https://github.com/miikka/cache-metrics"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories [["releases" :clojars]]
  :dependencies [[org.clojure/core.cache "0.8.2"]
                 [io.dropwizard.metrics/metrics-core "4.1.2"]]
  :plugins [[lein-auto "0.1.3"]
            [lein-eftest "0.5.2"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0"]]
                   :source-paths ["dev-src"]}})
