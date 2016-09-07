package io.sponges.bot.server.entities.message;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.message.SentMessage;
import io.sponges.bot.server.protocol.msg.DeleteMessage;

public class SentMessageImpl implements SentMessage {

    private final Client client;

    public SentMessageImpl(Client client) {
        this.client = client;
    }

    @Override
    public void delete() {
        new DeleteMessage(client, this).send();
    }

    @Override
    public void edit(String s) {
        // TODO message editing
    }
}
