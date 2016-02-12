package io.sponges.botserver.internal;

import io.sponges.botserver.event.events.ClientInputEvent;
import io.sponges.botserver.event.framework.EventManager;
import io.sponges.botserver.util.Msg;
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
