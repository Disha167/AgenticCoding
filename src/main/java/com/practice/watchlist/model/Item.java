package com.practice.watchlist.model;

/**
 * A listed item. Prices are stored in cents to avoid floating-point errors.
 * Every price change bumps the version, which callers use for optimistic locking.
 */
public class Item {
    private final String id;
    private final String title;
    private final long basePriceCents;
    private long currentPriceCents;
    private long version;

    public Item(String id, String title, long basePriceCents) {
        this.id = id;
        this.title = title;
        this.basePriceCents = basePriceCents;
        this.currentPriceCents = basePriceCents;
        this.version = 1;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public long getBasePriceCents() { return basePriceCents; }
    public long getCurrentPriceCents() { return currentPriceCents; }
    public long getVersion() { return version; }

    public void applyPrice(long newPriceCents) {
        this.currentPriceCents = newPriceCents;
        this.version++;
    }
}
