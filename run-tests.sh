#!/usr/bin/env bash
# Compiles everything and runs the test suite. Needs JDK 17+.
set -e
cd "$(dirname "$0")"
rm -rf out
javac -d out $(find src -name "*.java")
java -cp out com.practice.watchlist.WatchlistTests
