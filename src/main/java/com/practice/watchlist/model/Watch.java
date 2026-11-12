package com.practice.watchlist.model;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/** A buyer watching an item, and the channels they want to be notified on. */
public class Watch {
    private final String buyerId;
    private final String itemId;
    private final Set<Channel> channels;

    public Watch(String buyerId, String itemId, Set<Channel> channels) {
        this.buyerId = buyerId;
        this.itemId = itemId;
        this.channels = channels.isEmpty() ? EnumSet.of(Channel.IN_APP) : EnumSet.copyOf(channels);
    }

    public String getBuyerId() { return buyerId; }
    public String getItemId() { return itemId; }
    public Set<Channel> getChannels() { return channels; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Watch)) return false;
        Watch other = (Watch) o;
        return buyerId.equals(other.buyerId)
                && itemId.equals(other.itemId)
                && channels.equals(other.channels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerId, itemId, channels);
    }
}
