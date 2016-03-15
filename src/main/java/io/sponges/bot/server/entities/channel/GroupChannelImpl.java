package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.storage.data.ChannelData;
import io.sponges.bot.server.entities.ClientImpl;

import java.util.HashMap;
import java.util.Map;

public class GroupChannelImpl implements GroupChannel {

    // TODO move user shit to NetworkImpl

    private final Map<String, User> users = new HashMap<>();

    private final String id;
    private final Network network;

    private ChannelData channelData = null;

    public GroupChannelImpl(String id, Network network) {
        this.id = id;
        this.network = network;
    }

    @Override
    public Map<String, User> getUsers() {
        return users;
    }

    @Override
    public boolean isUser(String s) {
        return users.containsKey(s);
    }

    @Override
    public User getUser(String s) {
        return users.get(s);
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

    public void setChannelData(ChannelData channelData) {
        this.channelData = channelData;
    }
}
