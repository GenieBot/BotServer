package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.entities.message.format.FormattedMessage;
import org.json.JSONObject;

public final class SendRawMessage extends Message {

    private final Network network;
    private final Channel channel;
    private final String message;
    private final boolean formatted;

    public SendRawMessage(Client client, Network network, Channel channel, String message) {
        super(client, "RAW");
        this.network = network;
        this.channel = channel;
        this.message = message;
        this.formatted = false;
    }

    public SendRawMessage(Client client, Network network, Channel channel, FormattedMessage message) {
        super(client, "RAW");
        this.network = network;
        this.channel = channel;
        this.message = message.getRaw();
        this.formatted = true;
    }

    @Override
    public JSONObject toJson() {
        JSONObject channel = new JSONObject();
        channel.put("id", this.channel.getSourceId());
        channel.put("private", channel instanceof PrivateChannel);

        return new JSONObject()
                .put("network", network.getSourceId())
                .put("channel", channel)
                .put("message", message)
                .put("formatted", formatted);
    }
}
