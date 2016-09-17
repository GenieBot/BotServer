package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.data.NetworkData;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.server.entities.data.NetworkDataImpl;
import io.sponges.bot.server.entities.manager.ChannelManagerImpl;
import io.sponges.bot.server.entities.manager.UserManagerImpl;

import java.util.UUID;

public class NetworkImpl implements Network {

    private final UUID id;
    private final String sourceId;
    private final Client client;
    private final ChannelManager channelManager;
    private final UserManager userManager;
    private final NetworkData networkData;

    public NetworkImpl(UUID id, String sourceId, Client client) {
        this.id = id;
        this.sourceId = sourceId;
        this.client = client;
        this.channelManager = new ChannelManagerImpl(this);
        this.userManager = new UserManagerImpl(this);
        this.networkData = new NetworkDataImpl();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }
}
