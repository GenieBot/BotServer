package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Message;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.server.protocol.msg.CmdResponseMessage;

public class CommandRequestImpl implements CommandRequest {

    private final Client client;
    private final Network network;
    private final Channel channel;
    private final User user;
    private final Message message;
    private String messageId = null;

    public CommandRequestImpl(Client client, Network network, Channel channel, User user, Message message) {
        this.client = client;
        this.network = network;
        this.channel = channel;
        this.user = user;
        this.message = message;
    }

    @Override
    public void reply(String s) {
        CmdResponseMessage message = new CmdResponseMessage(client, network, channel, user, s);
        if (messageId != null) message.setMessageId(messageId);
        channel.sendMessage(message.toString());
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
    public Message getMessage() {
        return message;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
