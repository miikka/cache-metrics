(ns release.changelog
  "Update the changelog for a release."
  (:require [clojure.string :as str]))

(defn parse-new-change
  [text]
  (let [lines (str/split-lines text)
        release-type (some->> lines first str/trim (re-find #"^RELEASE_TYPE: (major|minor|patch)$") second)
        release-text (str/trim (str/join "\n" (drop 2 lines)))]
    (when release-type
      (when-not (str/blank? (second lines))
        (throw (ex-info "Expected the second line to be blank" {:line (second lines)})))
      {:release-type (keyword release-type)
       :release-text release-text})))

(defn find-breakpoint
  "Find the index of the first line starting with `##`"
  [lines]
  (loop [idx 0, lines lines]
    (if (or (not lines) (str/starts-with? (first lines) "## "))
      idx
      (recur (inc idx) (next lines)))))

(defn insert-text [changelog new-text]
  (let [lines (str/split-lines changelog)
        breakpoint (find-breakpoint lines)]
    (str/join "\n" (concat (take breakpoint lines) [new-text] (drop breakpoint lines)))))

(defn format-change [version {:keys [release-text]}]
  (let [date (java.time.LocalDate/now)]
    (format "## %s (%s)\n\n%s\n" version date release-text)))

(defn read-current-version [] (read-string (slurp "version.edn")))

(defn bump-version [release-type {:keys [minor major patch]}]
  (case release-type
    :major {:major (inc major), :minor 0, :patch 0}
    :minor {:major major, :minor (inc minor), :patch 0}
    :patch {:major major, :minor minor, :patch (inc patch)}))

(defn format-version [{:keys [minor major patch]}] (str major "." minor "." patch))

(defn -main [& args]
  (if-let [release-info (parse-new-change (slurp "RELEASE.md"))]
    (let [changelog (slurp "CHANGELOG.md")
          new-version (bump-version (:release-type release-info) (read-current-version))
          version-str (format-version new-version)]
      (spit "version.edn" (pr-str new-version))
      (spit "CHANGELOG.md" (insert-text changelog (format-change version-str release-info)))
      (println version-str))
    (do
      (println "Unable to parse RELEASE.md.")
      (System/exit 1))))
