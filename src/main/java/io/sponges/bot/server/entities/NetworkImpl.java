package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.data.NetworkData;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.NetworkModuleManager;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.entities.data.NetworkDataImpl;
import io.sponges.bot.server.entities.manager.ChannelManagerImpl;
import io.sponges.bot.server.entities.manager.NetworkModuleManagerImpl;
import io.sponges.bot.server.entities.manager.UserManagerImpl;

import java.util.UUID;

public class NetworkImpl implements Network {

    private final UUID id;
    private final String sourceId;
    private final Client client;
    private final ChannelManager channelManager;
    private final UserManager userManager;
    private final NetworkData networkData;
    private final NetworkModuleManager moduleManager;

    public NetworkImpl(Bot bot, UUID id, String sourceId, Client client) {
        this.id = id;
        this.sourceId = sourceId;
        this.client = client;
        this.channelManager = new ChannelManagerImpl(bot.getDatabase(), this);
        this.userManager = new UserManagerImpl(bot.getDatabase(), this);
        this.networkData = new NetworkDataImpl();
        this.moduleManager = new NetworkModuleManagerImpl(this, bot);
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

    @Override
    public NetworkModuleManager getModuleManager() {
        return moduleManager;
    }
}
