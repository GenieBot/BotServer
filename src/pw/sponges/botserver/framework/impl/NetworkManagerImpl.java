package pw.sponges.botserver.framework.impl;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.framework.Network;
import pw.sponges.botserver.framework.NetworkManager;

import java.util.HashMap;
import java.util.Map;

public class NetworkManagerImpl implements NetworkManager {

    private final Client client;

    private final Map<String, Network> networks = new HashMap<>();

    public NetworkManagerImpl(Client client) {
        this.client = client;
    }

    @Override
    public boolean isNetwork(String id) {
        return networks.containsKey(id);
    }

    @Override
    public Network getNetwork(String id) {
        return networks.get(id);
    }

    @Override
    public Network getOrCreateNetwork(String id) {
        if (!isNetwork(id)) {
            return createNetwork(id);
        } else {
            return getNetwork(id);
        }
    }

    private Network createNetwork(String id) {
        Network network = new NetworkImpl(client, id);
        networks.put(id, network);
        return network;
    }
}
