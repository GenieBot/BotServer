package io.sponges.bot.server.entities.data;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.data.ChannelData;
import io.sponges.bot.server.protocol.msg.UpdateChannelDataMessage;

import java.util.Optional;

public class ChannelDataImpl implements ChannelData {

    private Optional<String> name = Optional.empty();
    private Optional<String> topic = Optional.empty();

    private final Channel channel;
    private final Network network;
    private final Client client;

    public ChannelDataImpl(Channel channel) {
        this.channel = channel;
        this.network = channel.getNetwork();
        this.client = this.network.getClient();
    }

    @Override
    public Optional<String> getName() {
        return name;
    }

    @Override
    public void updateName(String name) {
        this.name = Optional.of(name);
        new UpdateChannelDataMessage(client, network, channel, "name", name).send();
    }

    public void setName(String name) {
        this.name = Optional.of(name);
    }

    @Override
    public Optional<String> getTopic() {
        return topic;
    }

    @Override
    public void updateTopic(String topic) {
        this.topic = Optional.of(topic);
        new UpdateChannelDataMessage(client, network, channel, "topic", topic).send();
    }

    public void setTopic(String topic) {
        this.topic = Optional.of(topic);
    }
}
