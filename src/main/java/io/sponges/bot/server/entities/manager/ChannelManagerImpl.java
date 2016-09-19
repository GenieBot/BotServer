package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.select.SelectChannelStatement;
import io.sponges.bot.server.protocol.msg.ResourceRequestMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ChannelManagerImpl implements ChannelManager {

    private final Map<String, Channel> channels = new HashMap<>();

    private final Database database;
    private final Network network;

    public ChannelManagerImpl(Database database, Network network) {
        this.database = database;
        this.network = network;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public Map<String, Channel> getChannels() {
        return channels;
    }

    @Override
    public boolean isChannel(UUID s) {
        return channels.containsKey(s.toString());
    }

    @Override
    public Channel getChannel(UUID s) {
        return channels.get(s.toString());
    }

    private Channel getChannelBySourceId(String sourceId) {
        for (Channel channel : channels.values()) {
            if (channel.getSourceId().equals(sourceId)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public void loadChannel(String sourceId, Consumer<Channel> consumer) {
        Channel channel = getChannelBySourceId(sourceId);
        if (channel != null) {
            consumer.accept(channel);
            return;
        }
        new ResourceRequestMessage<Channel>(network.getClient(), network, ResourceRequestMessage.ResourceType.CHANNEL,
                sourceId, c -> {
            if (c != null) {
                channels.put(c.getId().toString(), c);
            }
            consumer.accept(c);
        }).send();
    }

    @Override
    public Channel loadChannelSync(String sourceId) {
        AtomicBoolean set = new AtomicBoolean(false);
        AtomicReference<Channel> net = new AtomicReference<>();
        loadChannel(sourceId, channel -> {
            set.set(true);
            net.set(channel);
        });
        while (!set.get()) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return net.get();
    }

    @Override
    public Channel loadChannelSync(UUID id) {
        String[] results;
        try {
            results = new SelectChannelStatement(database, id).executeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String sourceId = results[1];
        return loadChannelSync(sourceId);
    }

}
