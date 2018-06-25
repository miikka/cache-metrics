(ns cache-metrics.core-test
  (:require [clojure.test :refer :all]
            [clojure.core.cache :as cache]
            [cache-metrics.core :refer [cache-size-gauge instrumented-cache-factory]])
  (:import com.codahale.metrics.MetricRegistry))

(defn- get-gauge [registry name]
  (get (.getGauges registry) name))

(deftest hit-miss-test
  (let [registry (MetricRegistry.)
        cache-a (atom (-> {}
                          (cache/fifo-cache-factory :threshold 3)
                          (instrumented-cache-factory :registry registry)))]
    (.register registry "clojure.core.cache.size" (cache-size-gauge cache-a))
    (is (= 0 (.getValue (get-gauge registry "clojure.core.cache.size"))))
    (dotimes [i 6]
      (let [cache-key (mod i 3)]
        (is (= cache-key (cache/lookup (swap! cache-a cache/through cache-key) cache-key)))))
    (let [hits (.meter registry "clojure.core.cache.hits")
          misses (.meter registry "clojure.core.cache.misses")]
      (is (= 3 (.getCount hits)))
      (is (= 3 (.getCount misses))))
    (is (= 3 (.getValue (get-gauge registry "clojure.core.cache.size"))))))
