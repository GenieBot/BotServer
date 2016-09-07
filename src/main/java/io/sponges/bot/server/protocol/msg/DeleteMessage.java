package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.message.ReceivedMessage;
import io.sponges.bot.api.entities.message.SentMessage;
import org.json.JSONObject;

public class DeleteMessage extends Message {

    // TODO to delete messages there needs to be a system to identify each message on the client

    public DeleteMessage(Client client, ReceivedMessage message) {
        super(client, "DELETE_MESSAGE");
    }

    public DeleteMessage(Client client, SentMessage message) {
        super(client, "DELETE_MESSAGE");
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
