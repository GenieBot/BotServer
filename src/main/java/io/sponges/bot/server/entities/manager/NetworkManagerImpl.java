package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.manager.NetworkManager;

import java.util.HashMap;
import java.util.Map;

public class NetworkManagerImpl implements NetworkManager {

    private final Map<String, Network> networks = new HashMap<>();

    private final Client client;

    public NetworkManagerImpl(Client client) {
        this.client = client;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Map<String, Network> getNetworks() {
        return networks;
    }

    @Override
    public boolean isNetwork(String id) {
        return networks.containsKey(id);
    }

    @Override
    public Network getNetwork(String id) {
        return networks.get(id);
    }
}
