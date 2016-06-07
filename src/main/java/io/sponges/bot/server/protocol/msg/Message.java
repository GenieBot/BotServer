package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.server.entities.ClientImpl;
import org.json.JSONObject;

public abstract class Message {

    private final Client client;
    private final String type;

    private String messageId = null;

    public Message(Client client, String type) {
        this.client = client;
        this.type = type;
    }

    public abstract JSONObject toJson();

    public JSONObject getAsJson() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("client", client.getId());
        json.put("time", System.currentTimeMillis());
        if (this.messageId != null) json.put("id", messageId);
        json.put("content", toJson());
        return json;
    }

    @Override
    public String toString() {
        return getAsJson().toString();
    }

    public void send() {
        ((ClientImpl) client).write(toString());
    }

    @Deprecated
    public void send(ClientImpl client) {
        send();
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
