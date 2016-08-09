package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.message.SentMessage;
import io.sponges.bot.api.entities.message.format.FormattedMessage;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.entities.data.ChannelDataImpl;
import io.sponges.bot.server.entities.message.SentMessageImpl;
import io.sponges.bot.server.protocol.msg.SendRawMessage;

import java.util.HashMap;
import java.util.Map;

public class GroupChannelImpl implements GroupChannel {

    private static final String DATA_KEY = "clients:%s:networks:%s:channels:%s:data";

    // TODO move user shit to NetworkImpl

    private final Map<String, User> users = new HashMap<>();

    private final String id;
    private final Network network;
    private final io.sponges.bot.api.entities.data.ChannelData channelData;
    private final DataObject data;

    public GroupChannelImpl(String id, Network network, Storage storage) {
        this.id = id;
        this.network = network;
        this.channelData = new ChannelDataImpl(this);
        this.data = new DataObject(String.format(DATA_KEY, network.getClient().getId(), network.getId(), id));
        storage.load(this.data);
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
