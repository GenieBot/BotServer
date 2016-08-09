package io.sponges.bot.server.entities.message;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.message.Attachment;
import io.sponges.bot.api.entities.message.ReceivedMessage;

import java.util.Date;
import java.util.List;

public class ReceivedMessageImpl implements ReceivedMessage {

    private final Client client;
    private final Network network;
    private final Channel channel;
    private final User user;
    private final Date date;
    private final String content;
    private final List<Attachment> attachments;

    public ReceivedMessageImpl(Client client, Network network, Channel channel, User user, Date date, String content,
                               List<Attachment> attachments) {
        this.client = client;
        this.network = network;
        this.channel = channel;
        this.user = user;
        this.date = date;
        this.content = content;
        this.attachments = attachments;
    }


    @Override
    public void delete() throws CannotDeleteException {
        // TODO message deleting
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

    @Override
    public List<Attachment> getAttachments() {
        return attachments;
    }
}
