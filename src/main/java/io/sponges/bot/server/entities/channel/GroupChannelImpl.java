package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.message.SentMessage;
import io.sponges.bot.api.entities.message.format.FormattedMessage;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.entities.data.ChannelDataImpl;
import io.sponges.bot.server.entities.message.SentMessageImpl;
import io.sponges.bot.server.protocol.msg.SendRawMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupChannelImpl implements GroupChannel {

    private final Map<String, User> users = new HashMap<>();

    private final UUID id;
    private final String sourceId;
    private final Network network;
    private final io.sponges.bot.api.entities.data.ChannelData channelData;

    public GroupChannelImpl(UUID id, String sourceId, Network network) {
        this.id = id;
        this.sourceId = sourceId;
        this.network = network;
        this.channelData = new ChannelDataImpl(this);
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
        client.write(message);
    }

    @Override
    public SentMessage sendChatMessage(String s) {
        Client client = network.getClient();
        new SendRawMessage(client, network, this, s).send();
        return new SentMessageImpl(client);
    }

    @Override
    public SentMessage sendChatMessage(FormattedMessage formattedMessage) {
        Client client = network.getClient();
        new SendRawMessage(client, network, this, formattedMessage).send();
        return new SentMessageImpl(client);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public io.sponges.bot.api.entities.data.ChannelData getChannelData() {
        return channelData;
    }
}
