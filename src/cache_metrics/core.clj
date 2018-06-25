(ns cache-metrics.core
  "Dropwizard Metrics instrumentation for core.cache caches.

  ## Usage

  To count the cache hits and the misses, wrap the cache in
  [[instrumented-cache-factory]].

      (def my-registry (MetricsRegistry.))

      (defn create-cache []
        (-> (lru-cache-factory {})
            (instrumented-cache-factory :registry my-registry)))

  If you follow the usage pattern recommend by core.cache documentation and wrap
  the cache in an atom, you can measure the cache size with
  [[cache-size-gauge]].

      (def my-cache-a (atom (create-cache)))
      (.register my-registry \"clojure.core.cache.size\" (cache-size-gauge my-cache-a))"
  (:require [clojure.core.cache :as cache])
  (:import com.codahale.metrics.Gauge))

(cache/defcache InstrumentedCache [cache metrics]
  cache/CacheProtocol
  (lookup [_ item]
    (cache/lookup cache item))
  (lookup [_ item not-found]
    (cache/lookup cache item not-found))
  (has? [_ item]
    (cache/has? cache item))
  (hit [_ item]
    (.mark (:hits metrics))
    (InstrumentedCache. (cache/hit cache item) metrics))
  (miss [_ item result]
    (.mark (:misses metrics))
    (InstrumentedCache. (cache/miss cache item result) metrics))
  (evict [_ key]
    (InstrumentedCache. (cache/evict cache key) metrics))
  (seed [_ base]
    (InstrumentedCache. (cache/seed cache base) metrics)))

(defn instrumented-cache-factory
  "Wrap base cache with instrumentation logic that counts the hits and the
  misses.

  Expects `:registry` argument that is the MetricRegistry for the metrics.
  Optionally `:metric-name` argument specifies the name prefix for the metircs."
  [base & {:keys [registry metric-name]
           :or {metric-name "clojure.core.cache"}}]
  {:pre [(some? registry)
         (satisfies? cache/CacheProtocol base)]}
  (let [metrics {:hits   (.meter registry (str metric-name ".hits"))
                 :misses (.meter registry (str metric-name ".misses"))}]
    (InstrumentedCache. base metrics)))

(defn cache-size-gauge [cache-a]
  (proxy [Gauge] []
    (getValue [] (count @cache-a))))
