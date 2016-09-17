package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.entities.data.ChannelData;
import io.sponges.bot.api.entities.message.SentMessage;
import io.sponges.bot.api.entities.message.format.FormattedMessage;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.entities.data.ChannelDataImpl;
import io.sponges.bot.server.entities.message.SentMessageImpl;
import io.sponges.bot.server.protocol.msg.SendRawMessage;

import java.util.UUID;

public class PrivateChannelImpl implements PrivateChannel {

    private final UUID id;
    private final String sourceId;
    private final Network network;
    private final io.sponges.bot.api.entities.data.ChannelData channelData;

    private User user = null;

    public PrivateChannelImpl(UUID id, String sourceId, Network network) {
        this.id = id;
        this.sourceId = sourceId;
        this.network = network;
        this.channelData = new ChannelDataImpl(this);
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
    public ChannelData getChannelData() {
        return channelData;
    }
}
