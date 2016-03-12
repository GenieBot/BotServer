package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.storage.ChannelData;
import io.sponges.bot.server.entities.ClientImpl;

public class PrivateChannelImpl implements PrivateChannel {

    private final String id;
    private final Network network;
    private final ChannelData channelData;
    private final User user;

    public PrivateChannelImpl(String id, Network network, ChannelData channelData, User user) {
        this.id = id;
        this.network = network;
        this.channelData = channelData;
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void sendMessage(String message) {
        ClientImpl client = (ClientImpl) network.getClient();
        client.getChannel().writeAndFlush(message + "\r\n");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public ChannelData getData() {
        return channelData;
    }
}
