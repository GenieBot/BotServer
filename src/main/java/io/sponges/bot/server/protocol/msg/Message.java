package io.sponges.bot.server.protocol.msg;

import io.netty.channel.Channel;
import io.sponges.bot.api.entities.Client;
import org.json.JSONObject;

public abstract class Message {

    private final Client client;
    private final String type;

    protected Message(Client client, String type) {
        this.client = client;
        this.type = type;
    }

    protected abstract JSONObject toJson();

    public JSONObject getAsJson() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("client", client.getId());
        json.put("time", System.currentTimeMillis());
        json.put("content", toJson());
        return json;
    }

    @Override
    public String toString() {
        return getAsJson().toString();
    }

    public void send(Channel channel) {
        channel.writeAndFlush(toString() + "\r\n");
    }
}
