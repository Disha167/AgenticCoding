package com.practice.watchlist;

import com.practice.watchlist.model.Channel;
import com.practice.watchlist.model.Watch;
import com.practice.watchlist.repo.WatchRepository;

import java.util.EnumSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stage 3 drill. 16 threads add 1,000 distinct watchers each to the same popular item.
 * Expected: 16000 watchers, 0 errors. Run with ./run-drill.sh (results vary run to run).
 */
public class ConcurrencyDrill {
    public static void main(String[] args) throws Exception {
        int threads = 16, perThread = 1_000;
        WatchRepository repo = new WatchRepository();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger errors = new AtomicInteger();

        for (int t = 0; t < threads; t++) {
            int threadId = t;
            pool.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < perThread; i++) {
                        repo.add(new Watch("B" + threadId + "-" + i, "HOT", EnumSet.of(Channel.PUSH)));
                    }
                } catch (Throwable e) {
                    errors.incrementAndGet();
                }
            });
        }
        start.countDown();
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("expected watchers: " + threads * perThread);
        System.out.println("actual watchers:   " + repo.count("HOT"));
        System.out.println("thread errors:     " + errors.get());
    }
}
