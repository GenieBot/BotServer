package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.server.protocol.msg.ResourceRequestMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class NetworkManagerImpl implements NetworkManager {

    private final Map<String, Network> networks = new ConcurrentHashMap<>();

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

    @Override
    public void loadNetwork(String networkId, Consumer<Network> consumer) {
        if (isNetwork(networkId)) {
            consumer.accept(getNetwork(networkId));
            return;
        }
        new ResourceRequestMessage(client, networkId, entity -> {
            Network network = (Network) entity;
            networks.put(network.getId(), network);
            consumer.accept(network);
        }).send();
    }
}
