package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.storage.ChannelData;

import java.util.HashMap;
import java.util.Map;

public class GroupChannelImpl implements GroupChannel {

    private final Map<String, User> users = new HashMap<>();

    private final String id;
    private final Network network;
    private final ChannelData channelData;

    public GroupChannelImpl(String id, Network network, ChannelData channelData) {
        this.id = id;
        this.network = network;
        this.channelData = channelData;
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
    public void sendMessage(String s) {
        // TODO message sending
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
