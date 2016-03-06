package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import org.json.JSONObject;

public final class SendRawMessage extends Message {

    private final Network network;
    private final Channel channel;
    private final String message;

    public SendRawMessage(Client client, Network network, Channel channel, String message) {
        super(client, "RAW");
        this.network = network;
        this.channel = channel;
        this.message = message;
    }

    @Override
    protected JSONObject toJson() {
        return new JSONObject()
                .put("network", network.getId())
                .put("channel", channel.getId())
                .put("message", message);
    }
}
