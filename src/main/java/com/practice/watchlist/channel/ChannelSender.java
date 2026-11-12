package com.practice.watchlist.channel;

import com.practice.watchlist.model.Notification;

/** Sends one notification on one channel (email provider, push service, in-app inbox). */
public interface ChannelSender {
    void send(Notification notification);
}
