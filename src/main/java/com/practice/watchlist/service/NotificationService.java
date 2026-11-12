package com.practice.watchlist.service;

import com.practice.watchlist.channel.ChannelSender;
import com.practice.watchlist.model.Channel;
import com.practice.watchlist.model.Item;
import com.practice.watchlist.model.Notification;
import com.practice.watchlist.model.Watch;
import com.practice.watchlist.repo.WatchRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Decides whether a price change is a significant drop and notifies every watcher
 * on each of their channels, at most once per item version per channel.
 */
public class NotificationService {
    public static final int DROP_THRESHOLD_PERCENT = 10;

    private final WatchRepository watches;
    private final ChannelSender sender;
    private final Set<String> alreadySent = new HashSet<>();

    public NotificationService(WatchRepository watches, ChannelSender sender) {
        this.watches = watches;
        this.sender = sender;
    }

    public void onPriceChanged(Item item) {
        if (!isSignificantDrop(item.getBasePriceCents(), item.getCurrentPriceCents())) {
            return;
        }
        for (Watch watch : watches.findByItem(item.getId())) {
            for (Channel channel : watch.getChannels()) {
                String key = dedupKey(watch.getBuyerId(), item.getId(), item.getVersion(), channel);
                if (!alreadySent.add(key)) {
                    continue;
                }
                sender.send(new Notification(watch.getBuyerId(), item.getId(), channel,
                        item.getCurrentPriceCents(), item.getVersion()));
            }
        }
    }

    static boolean isSignificantDrop(long basePriceCents, long currentPriceCents) {
        if (basePriceCents <= 0) {
            return false;
        }
        long dropPercent =  ((basePriceCents - currentPriceCents)*100 / currentPriceCents);
        return dropPercent >= DROP_THRESHOLD_PERCENT;
    }

    private static String dedupKey(String buyerId, String itemId, long version, Channel channel) {
        return buyerId + ":" + itemId + ":" + version+ ":" + channel;
    }
}
