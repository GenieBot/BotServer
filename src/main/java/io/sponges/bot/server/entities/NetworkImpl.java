package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.RoleManager;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.api.storage.data.NetworkData;
import io.sponges.bot.server.entities.manager.ChannelManagerImpl;
import io.sponges.bot.server.entities.manager.RoleManagerImpl;
import io.sponges.bot.server.entities.manager.UserManagerImpl;

public class NetworkImpl implements Network {

    private final String id;
    private final Client client;
    private final ChannelManager channelManager;
    private final UserManager userManager;
    private final RoleManager roleManager;

    private NetworkData networkData = null;

    public NetworkImpl(String id, Client client) {
        this.id = id;
        this.client = client;
        this.channelManager = new ChannelManagerImpl(this);
        this.userManager = new UserManagerImpl(this);
        this.roleManager = new RoleManagerImpl(this);
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

    public void setNetworkData(NetworkData networkData) {
        this.networkData = networkData;
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
    public RoleManager getRoleManager() {
        return roleManager;
    }
}
