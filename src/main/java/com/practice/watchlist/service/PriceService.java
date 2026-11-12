package com.practice.watchlist.service;

import com.practice.watchlist.model.Item;
import com.practice.watchlist.repo.ItemRepository;

/** Handles seller price updates. Uses the item version for optimistic locking. */
public class PriceService {
    private final ItemRepository items;
    private final NotificationService notifications;

    public PriceService(ItemRepository items, NotificationService notifications) {
        this.items = items;
        this.notifications = notifications;
    }

    /**
     * Updates the price if the caller saw the latest version.
     *
     * @return true if applied, false if the caller's version is stale
     */
    public boolean updatePrice(String itemId, long newPriceCents, long expectedVersion) {
        Item item = items.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown item " + itemId));
        if (newPriceCents <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (item.getVersion() < expectedVersion) {
            return false;
        }
        long oldPriceCents = item.getCurrentPriceCents();
        item.applyPrice(newPriceCents);
        if (newPriceCents < oldPriceCents) {
            notifications.onPriceChanged(item);
        }
        return true;
    }
}
