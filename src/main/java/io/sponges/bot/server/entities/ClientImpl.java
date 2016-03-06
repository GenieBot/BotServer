package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.server.entities.manager.NetworkManagerImpl;

/**
 * Implementation of the Client interface
 */
public class ClientImpl implements Client {

    private final String id;
    private final NetworkManager networkManager;

    public ClientImpl(String id) {
        this.id = id;
        this.networkManager = new NetworkManagerImpl(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}
