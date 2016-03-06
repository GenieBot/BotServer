package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.storage.NetworkData;

public class NetworkImpl implements Network {

    private final String id;
    private final Client client;
    private final NetworkData networkData;
    private final ChannelManager channelManager;

    public NetworkImpl(String id, Client client, NetworkData networkData, ChannelManager channelManager) {
        this.id = id;
        this.client = client;
        this.networkData = networkData;
        this.channelManager = channelManager;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public NetworkData getData() {
        return networkData;
    }

    @Override
    public ChannelManager getChannelManager() {
        return channelManager;
    }
}
