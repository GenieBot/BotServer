package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.PrivateChannel;
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
        JSONObject channel = new JSONObject();
        channel.put("id", this.channel.getId());
        channel.put("private", channel instanceof PrivateChannel);

        return new JSONObject()
                .put("network", network.getId())
                .put("channel", channel)
                .put("message", message);
    }
}
