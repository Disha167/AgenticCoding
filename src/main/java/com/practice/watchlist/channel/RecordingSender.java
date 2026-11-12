package com.practice.watchlist.channel;

import com.practice.watchlist.model.Notification;

import java.util.ArrayList;
import java.util.List;

/** Test double that records everything it is asked to send. */
public class RecordingSender implements ChannelSender {
    private final List<Notification> sent = new ArrayList<>();

    @Override
    public void send(Notification notification) {
        sent.add(notification);
    }

    public List<Notification> getSent() {
        return sent;
    }
}
