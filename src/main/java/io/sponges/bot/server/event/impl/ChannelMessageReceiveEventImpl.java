package io.sponges.bot.server.event.impl;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.event.events.channelmsg.ChannelMessageReceiveEvent;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.protocol.msg.ChannelMessage;

public final class ChannelMessageReceiveEventImpl extends ChannelMessageReceiveEvent {

    public ChannelMessageReceiveEventImpl(Client client, String message, String messageId) {
        super(client, message, messageId);
    }

    @Override
    public void reply(String s) {
        ((ClientImpl) getClient()).write(new ChannelMessage(getClient(), getMessageId(), s, null, ChannelMessage.MessageType.RESPONSE)
                .toString());
    }
}
