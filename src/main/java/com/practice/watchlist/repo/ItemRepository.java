package com.practice.watchlist.repo;

import com.practice.watchlist.model.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** In-memory stand-in for the items table. */
public class ItemRepository {
    private final Map<String, Item> items = new HashMap<>();

    public void save(Item item) {
        items.put(item.getId(), item);
    }

    public Optional<Item> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }
}
