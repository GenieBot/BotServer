package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.select.SelectNetworkStatement;
import io.sponges.bot.server.protocol.msg.ResourceRequestMessage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class NetworkManagerImpl implements NetworkManager {

    private final Map<String, Network> networks = new ConcurrentHashMap<>();

    private final Database database;
    private final Client client;

    public NetworkManagerImpl(Database database, Client client) {
        this.database = database;
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
    public boolean isNetwork(UUID id) {
        return networks.containsKey(id.toString());
    }

    @Override
    public Network getNetwork(UUID id) {
        return networks.get(id.toString());
    }

    private Network getNetworksBySourceId(String sourceId) {
        for (Network network : networks.values()) {
            if (network.getSourceId().equals(sourceId)) {
                return network;
            }
        }
        return null;
    }

    @Override
    public void loadNetwork(String sourceId, Consumer<Network> consumer) {
        Network network = getNetworksBySourceId(sourceId);
        if (network != null) {
            consumer.accept(network);
            return;
        }
        new ResourceRequestMessage<Network>(client, sourceId, n -> {
            if (n != null) {
                networks.put(n.getId().toString(), n);
            }
            consumer.accept(n);
        }).send();
    }

    @Override
    public Network loadNetworkSync(String sourceId) {
        AtomicBoolean set = new AtomicBoolean(false);
        AtomicReference<Network> net = new AtomicReference<>();
        loadNetwork(sourceId, network -> {
            set.set(true);
            net.set(network);
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
    public Network loadNetworkSync(UUID id) {
        String[] results;
        try {
            results = new SelectNetworkStatement(database, id).executeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String sourceId = results[1];
        return loadNetworkSync(sourceId);
    }
}
