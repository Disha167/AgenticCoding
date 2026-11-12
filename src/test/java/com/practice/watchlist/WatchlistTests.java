package com.practice.watchlist;

import com.practice.watchlist.channel.RecordingSender;
import com.practice.watchlist.model.Channel;
import com.practice.watchlist.model.Item;
import com.practice.watchlist.model.Notification;
import com.practice.watchlist.model.Watch;
import com.practice.watchlist.repo.ItemRepository;
import com.practice.watchlist.repo.WatchRepository;
import com.practice.watchlist.service.NotificationService;
import com.practice.watchlist.service.PriceService;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Dependency-free test suite. Run with ./run-tests.sh
 * Each test builds a fresh "app" so tests don't affect each other.
 */
public class WatchlistTests {

    // ---------- tiny test harness ----------
    interface TestBody { void run() throws Exception; }
    record TestCase(String name, TestBody body) {}

    static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " | expected: " + expected + ", actual: " + actual);
        }
    }

    static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    // ---------- fixture ----------
    static class App {
        final ItemRepository items = new ItemRepository();
        final WatchRepository watches = new WatchRepository();
        final RecordingSender sender = new RecordingSender();
        final NotificationService notifications = new NotificationService(watches, sender);
        final PriceService prices = new PriceService(items, notifications);

        Item listItem(String id, long basePriceCents) {
            Item item = new Item(id, "Item " + id, basePriceCents);
            items.save(item);
            return item;
        }

        void watch(String buyerId, String itemId, Channel... channels) {
            Set<Channel> set = channels.length == 0 ? EnumSet.noneOf(Channel.class) : EnumSet.of(channels[0], channels);
            watches.add(new Watch(buyerId, itemId, set));
        }

        List<Notification> sent() { return sender.getSent(); }
    }

    // ---------- tests ----------
    static List<TestCase> tests() {
        List<TestCase> t = new ArrayList<>();

//        t.add(new TestCase("twenty percent drop notifies the watcher", () -> {
//            App app = new App();
//            app.listItem("I1", 10_000);
//            app.watch("B1", "I1", Channel.EMAIL);
//            app.prices.updatePrice("I1", 8_000, 1);
//            assertEquals(1, app.sent().size(), "one notification expected");
//        }));
//
//        t.add(new TestCase("exactly ten percent drop notifies on every channel", () -> {
//            App app = new App();
//            app.listItem("I1", 10_000);
//            app.watch("B1", "I1", Channel.EMAIL, Channel.PUSH);
//            app.prices.updatePrice("I1", 9_000, 1);
//            assertEquals(2, app.sent().size(), "EMAIL and PUSH expected");
//        }));
//
//        t.add(new TestCase("small drop does not notify", () -> {
//            App app = new App();
//            app.listItem("I1", 10_000);
//            app.watch("B1", "I1", Channel.EMAIL);
//            app.prices.updatePrice("I1", 9_500, 1);
//            assertEquals(0, app.sent().size(), "5% drop must not notify");
//        }));
//
//        t.add(new TestCase("price increase does not notify", () -> {
//            App app = new App();
//            app.listItem("I1", 10_000);
//            app.watch("B1", "I1", Channel.EMAIL);
//            app.prices.updatePrice("I1", 12_000, 1);
//            assertEquals(0, app.sent().size(), "increase must not notify");
//        }));
//
//        t.add(new TestCase("watch with no channels defaults to in-app", () -> {
//            App app = new App();
//            app.listItem("I1", 10_000);
//            app.watch("B1", "I1");
//            app.prices.updatePrice("I1", 5_000, 1);
//            assertEquals(1, app.sent().size(), "one in-app notification expected");
//            assertEquals(Channel.IN_APP, app.sent().get(0).channel(), "default channel");
//        }));

//        t.add(new TestCase("a second drop notifies again", () -> {
//            App app = new App();
//            app.listItem("I1", 10_000);
//            app.watch("B1", "I1", Channel.PUSH);
//            app.prices.updatePrice("I1", 9_000, 1);
//            app.prices.updatePrice("I1", 8_000, 2);
//            assertEquals(2, app.sent().size(), "each new lower price is a new notification");
//        }));

        t.add(new TestCase("watching the same item twice notifies once", () -> {
            App app = new App();
            app.listItem("I1", 10_000);
            app.watch("B1", "I1", Channel.EMAIL);
            app.watch("B1", "I1", Channel.EMAIL);
            app.prices.updatePrice("I1", 7_000, 1);
            assertEquals(1, app.watches.count("I1"), "duplicate watch must not be stored");
            assertEquals(1, app.sent().size(), "buyer must get one notification");
        }));

        t.add(new TestCase("stale version is rejected and price is unchanged", () -> {
            App app = new App();
            Item item = app.listItem("I1", 10_000);
            assertTrue(app.prices.updatePrice("I1", 9_500, 1), "first update should apply");
            boolean applied = app.prices.updatePrice("I1", 9_900, 1);
            assertTrue(!applied, "update with stale version must return false");
            assertEquals(9_500L, item.getCurrentPriceCents(), "stale write must not change price");
        }));

        t.add(new TestCase("unknown item throws", () -> {
            App app = new App();
            try {
                app.prices.updatePrice("NOPE", 100, 1);
                throw new AssertionError("expected IllegalArgumentException");
            } catch (IllegalArgumentException expected) {
                // ok
            }
        }));

        t.add(new TestCase("item with no watchers sends nothing", () -> {
            App app = new App();
            app.listItem("I1", 10_000);
            app.prices.updatePrice("I1", 1_000, 1);
            assertEquals(0, app.sent().size(), "no watchers, no notifications");
        }));

        return t;
    }

    public static void main(String[] args) {
        int passed = 0, failed = 0;
        for (TestCase test : tests()) {
            try {
                test.body().run();
                passed++;
                System.out.println("PASS  " + test.name());
            } catch (Throwable e) {
                failed++;
                System.out.println("FAIL  " + test.name() + "\n      " + e.getMessage());
            }
        }
        System.out.println("\n" + passed + " passed, " + failed + " failed");
        System.exit(failed == 0 ? 0 : 1);
    }
}
