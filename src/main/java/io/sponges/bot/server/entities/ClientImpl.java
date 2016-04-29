package io.sponges.bot.server.entities;

import io.netty.channel.Channel;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.server.entities.manager.NetworkManagerImpl;
import io.sponges.bot.server.protocol.msg.ChannelMessage;

import java.util.function.Consumer;

/**
 * Implementation of the Client interface
 */
public class ClientImpl implements Client {

    private final String id;
    private final String defaultPrefix;
    private final Channel channel;
    private final NetworkManager networkManager;

    public ClientImpl(String id, String defaultPrefix, Channel channel) {
        this.id = id;
        this.defaultPrefix = defaultPrefix;
        this.channel = channel;
        this.networkManager = new NetworkManagerImpl(this);
    }

    public void write(String message) {
        channel.writeAndFlush(message + "\r\n");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDefaultPrefix() {
        return defaultPrefix;
    }


    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public void sendMessage(String s, Consumer<String> consumer) {
        /*
        TODO implement this
        1) find some way to register the messages sent
        2) register the instance of the consumer
        3) send the received messages back to the consumer
         */
        new ChannelMessage(this, s, consumer).send();
    }
}
