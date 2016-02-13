package io.sponges.bot.server.internal;

import io.sponges.bot.server.event.events.ClientInputEvent;
import io.sponges.bot.server.event.framework.EventManager;
import io.sponges.bot.server.util.Msg;
import redis.clients.jedis.JedisPubSub;

public class ServerSubscriber extends JedisPubSub {

    private final Server server;
    private final EventManager eventManager;

    public ServerSubscriber(ServerImpl server) {
        this.server = server;
        this.eventManager = server.getBot().getEventManager();
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals("server")) return;
        eventManager.handle(new ClientInputEvent(message));
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
