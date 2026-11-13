package com.practice.watchlist.repo;

import com.practice.watchlist.model.Watch;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** In-memory stand-in for the watches table, indexed by item. */
public class WatchRepository {
    private final ConcurrentHashMap<String, Set<Watch>> watchesByItem = new ConcurrentHashMap<>();

    public void add(Watch watch) {
        // Set ignores a second watch with the same buyer, item and channels (see Watch.equals)
        Set<Watch> watches=watchesByItem.computeIfAbsent(watch.getItemId(), k -> new LinkedHashSet<>());
        synchronized (watches) {
            watches.add(watch);
        }


    }

//    public void remove(String buyerId, String itemId) {
//        Set<Watch> watches = watchesByItem.get(itemId);
//        if (watches != null) {
//            watches.removeIf(w -> w.getBuyerId().equals(buyerId));
//        }
//    }

    public List<Watch> findByItem(String itemId) {
         Set<Watch> watches=watchesByItem.getOrDefault(itemId, Set.of());
        synchronized (watches) {
            return List.copyOf(watches);
        }
    }

    public int count(String itemId) {
        Set<Watch> watches=watchesByItem.getOrDefault(itemId, Set.of());
        synchronized (watches){
            return watches.size();
        }
    }
}
