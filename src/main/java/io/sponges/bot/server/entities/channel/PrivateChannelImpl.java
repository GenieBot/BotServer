package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.storage.data.ChannelData;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.entities.data.ChannelDataImpl;
import io.sponges.bot.server.protocol.msg.SendRawMessage;

public class PrivateChannelImpl implements PrivateChannel {

    private final String id;
    private final Network network;
    private final io.sponges.bot.api.entities.data.ChannelData channelData;

    private User user = null;
    private ChannelData data = null;

    public PrivateChannelImpl(String id, Network network) {
        this.id = id;
        this.network = network;
        this.channelData = new ChannelDataImpl();
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void sendMessage(String message) {
        ClientImpl client = (ClientImpl) network.getClient();
        client.write(message);
    }

    @Override
    public void sendChatMessage(String s) {
        String message = new SendRawMessage(network.getClient(), network, this, s).toString();
        sendMessage(message);
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
    public io.sponges.bot.api.entities.data.ChannelData getChannelData() {
        return channelData;
    }

    @Override
    public ChannelData getData() {
        return data;
    }

    public void setChannelData(ChannelData data) {
        this.data = data;
    }
}
