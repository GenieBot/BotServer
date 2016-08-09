package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.entities.message.SentMessage;
import io.sponges.bot.api.entities.message.format.FormattedMessage;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.entities.data.ChannelDataImpl;
import io.sponges.bot.server.entities.message.SentMessageImpl;
import io.sponges.bot.server.protocol.msg.SendRawMessage;

public class PrivateChannelImpl implements PrivateChannel {

    private static final String DATA_KEY = "clients:%s:networks:%s:channels:%s:data";

    private final String id;
    private final Network network;
    private final io.sponges.bot.api.entities.data.ChannelData channelData;
    private final DataObject data;

    private User user = null;

    public PrivateChannelImpl(String id, Network network, Storage storage) {
        this.id = id;
        this.network = network;
        this.channelData = new ChannelDataImpl(this);
        this.data = new DataObject(String.format(DATA_KEY, network.getClient().getId(), network.getId(), id));
        storage.load(this.data);
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
        new SendRawMessage(network.getClient(), network, this, s).send();
        return new SentMessageImpl();
    }

    @Override
    public SentMessage sendChatMessage(FormattedMessage formattedMessage) {
        new SendRawMessage(network.getClient(), network, this, formattedMessage).send();
        return new SentMessageImpl();
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
    public DataObject getData() {
        return data;
    }
}
