#!/bin/bash

set -euo pipefail

if [ ! -f "RELEASE.md" ]; then
    echo "No RELEASE.md; not going to do anything."
    exit 0
fi

# Bump the changelog

NEW_VERSION=$(lein run -m release.changelog)
sed -i.sedbak "s/defproject \([[:graph:]]*\) \".*\"/defproject \1 \"$NEW_VERSION-SNAPSHOT\"/" project.clj
sed -i.sedbak "s/\[cache-metrics \".*\"\]/\[cache-metrics \"$NEW_VERSION\"\]/" README.md
rm *.sedbak

git add version.edn CHANGELOG.md project.clj README.md
git rm RELEASE.md
git commit -m "Release notes for $NEW_VERSION [skip ci]"

# Push to Clojars

lein release
