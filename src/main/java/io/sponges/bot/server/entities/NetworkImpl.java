package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.data.NetworkData;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.ModuleDataManager;
import io.sponges.bot.api.entities.manager.RoleManager;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.entities.data.NetworkDataImpl;
import io.sponges.bot.server.entities.manager.ChannelManagerImpl;
import io.sponges.bot.server.entities.manager.ModuleDataManagerImpl;
import io.sponges.bot.server.entities.manager.RoleManagerImpl;
import io.sponges.bot.server.entities.manager.UserManagerImpl;

public class NetworkImpl implements Network {

    private static final String DATA_KEY = "clients:%s:networks:%s:data";

    private final String id;
    private final Client client;
    private final ChannelManager channelManager;
    private final UserManager userManager;
    private final RoleManagerImpl roleManager;
    private final DataObject data;
    private final ModuleDataManager moduleDataManager;
    private final NetworkData networkData;

    public NetworkImpl(String id, Client client, Storage storage) {
        this.id = id;
        this.client = client;
        this.channelManager = new ChannelManagerImpl(this);
        this.userManager = new UserManagerImpl(this);
        this.roleManager = new RoleManagerImpl(storage, this);
        this.networkData = new NetworkDataImpl();
        this.data = new DataObject(String.format(DATA_KEY, client.getId(), id));
        storage.load(this.data);
        if (!data.exists("roles")) {
            this.data.set(storage, "roles", this.roleManager.getData());
        }
        this.moduleDataManager = new ModuleDataManagerImpl(storage, this.data);
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

    @Override
    public DataObject getData() {
        return data;
    }

    @Override
    public ModuleDataManager getModuleDataManager() {
        return moduleDataManager;
    }

    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }
}
