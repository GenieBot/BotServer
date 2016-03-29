package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import org.json.JSONObject;

public final class ChangeChannelTopicMessage extends Message {

    private final Network network;
    private final Channel channel;
    private final String topic;

    public ChangeChannelTopicMessage(Client client, Network network, Channel channel, String topic) {
        super(client, "CHANGE_CHANNEL_TOPIC");
        this.network = network;
        this.channel = channel;
        this.topic = topic;
    }

    @Override
    public JSONObject toJson() {
        JSONObject channel = new JSONObject();
        channel.put("id", this.channel.getId());

        return new JSONObject()
                .put("network", network.getId())
                .put("channel", channel)
                .put("topic", topic);
    }
}
