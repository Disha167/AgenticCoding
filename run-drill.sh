#!/usr/bin/env bash
# Stage 3: runs the concurrency drill. Needs JDK 17+.
set -e
cd "$(dirname "$0")"
rm -rf out
javac -d out $(find src -name "*.java")
java -cp out com.practice.watchlist.ConcurrencyDrill
