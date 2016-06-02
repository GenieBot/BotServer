package io.sponges.bot.server.entities.data;

import io.sponges.bot.api.entities.data.ChannelData;

import java.util.Optional;

public class ChannelDataImpl implements ChannelData {

    private Optional<String> name = Optional.empty();
    private Optional<String> topic = Optional.empty();

    @Override
    public Optional<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Optional.of(name);
    }

    @Override
    public Optional<String> getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = Optional.of(topic);
    }
}
