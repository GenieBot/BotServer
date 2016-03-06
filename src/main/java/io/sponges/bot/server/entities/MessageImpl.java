package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;

import java.util.Date;

public class MessageImpl implements Message {

    private final Client client;
    private final Network network;
    private final Channel channel;
    private final User user;
    private final Date date;
    private final String content;

    public MessageImpl(Client client, Network network, Channel channel, User user, Date date, String content) {
        this.client = client;
        this.network = network;
        this.channel = channel;
        this.user = user;
        this.date = date;
        this.content = content;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getContent() {
        return content;
    }
}
