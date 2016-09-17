package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import org.json.JSONObject;

public final class UpdateChannelDataMessage extends Message {

    private final Network network;
    private final Channel channel;
    private final String key;
    private final String value;

    public UpdateChannelDataMessage(Client client, Network network, Channel channel, String key, String value) {
        super(client, "UPDATE_CHANNEL_DATA");
        this.network = network;
        this.channel = channel;
        this.key = key;
        this.value = value;
    }

    @Override
    public JSONObject toJson() {
        JSONObject channel = new JSONObject();
        channel.put("id", this.channel.getSourceId());

        return new JSONObject()
                .put("network", network.getSourceId())
                .put("channel", channel)
                .put("detail", key)
                .put("value", value);
    }
}
