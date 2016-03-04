package io.sponges.bot.server.internal;

import io.sponges.bot.server.event.events.ClientInputEvent;
import io.sponges.bot.server.event.framework.EventBus;
import io.sponges.bot.server.util.Msg;
import redis.clients.jedis.JedisPubSub;

import java.util.List;

public class ServerSubscriber extends JedisPubSub {

    private final Server server;
    private final EventBus eventBus;
    private final List<String> channels;

    public ServerSubscriber(ServerImpl server) {
        this.server = server;
        this.eventBus = server.getBot().getEventBus();
        this.channels = server.getChannels();
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!channels.contains(channel)) return;
        eventBus.post(new ClientInputEvent(message));
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        Msg.log("[Jedis] Subscribed to " + channel + "!");
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        Msg.log("[Jedis] Unsubscribed from " + channel + "!");
    }
}
