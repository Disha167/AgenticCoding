package com.practice.watchlist.repo;

import com.practice.watchlist.model.Watch;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** In-memory stand-in for the watches table, indexed by item. */
public class WatchRepository {
    private final Map<String, Set<Watch>> watchesByItem = new HashMap<>();

    public void add(Watch watch) {
        // Set ignores a second watch with the same buyer, item and channels (see Watch.equals)
        watchesByItem.computeIfAbsent(watch.getItemId(), k -> new LinkedHashSet<>()).add(watch);
    }

    public void remove(String buyerId, String itemId) {
        Set<Watch> watches = watchesByItem.get(itemId);
        if (watches != null) {
            watches.removeIf(w -> w.getBuyerId().equals(buyerId));
        }
    }

    public List<Watch> findByItem(String itemId) {
        return List.copyOf(watchesByItem.getOrDefault(itemId, Set.of()));
    }

    public int count(String itemId) {
        return watchesByItem.getOrDefault(itemId, Set.of()).size();
    }
}
