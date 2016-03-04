package io.sponges.bot.server;

import io.sponges.bot.server.framework.NetworkManager;
import io.sponges.bot.server.framework.impl.NetworkManagerImpl;
import io.sponges.bot.server.internal.Server;
import io.sponges.bot.server.messages.Message;
import io.sponges.bot.server.util.Msg;

/**
 * Implementation of the Client interface
 */
public class ClientImpl implements Client {

    private final String id, channel;
    private final Server server;
    private final NetworkManager networkManager;
    public ClientImpl(String id, String channel, Server server) {
        this.id = id;
        this.channel = channel;
        this.server = server;
        this.networkManager = new NetworkManagerImpl(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void sendMessage(Message message) {
        Msg.debug("[Sending message] " + message.toString());
        server.publish(message);
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

}
