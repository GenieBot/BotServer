package io.sponges.bot.server.messages;

import io.sponges.bot.server.Client;
import org.json.JSONObject;

public abstract class Message {

    // TODO improve protocol

    private final Client client;
    private final String type;

    public Message(Client client, String type) {
        this.client = client;
        this.type = type;
    }

    public abstract JSONObject toJson();

    public Client getClient() {
        return client;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

}
