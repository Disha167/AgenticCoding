package com.practice.watchlist.model;

public record Notification(String buyerId, String itemId, Channel channel,
                           long priceCents, long itemVersion) {
}
