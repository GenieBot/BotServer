package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.server.entities.ClientImpl;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientManagerImpl implements ClientManager {

    private final Map<String, Client> clients = new HashMap<>();
    private final Map<String, UUID> sourceIdCache = new HashMap<>();

    @Override
    public Map<String, Client> getClients() {
        return clients;
    }

    @Override
    public boolean isClient(UUID s) {
        return clients.containsKey(s.toString());
    }

    @Override
    public Client getClient(UUID s) {
        return clients.get(s.toString());
    }

    public Client getClient(SocketAddress address) {
        for (Client client : clients.values()) {
            if (((ClientImpl) client).getRemoteAddress() == address) {
                return client;
            }
        }
        return null;
    }

    public Map<String, UUID> getSourceIdCache() {
        return sourceIdCache;
    }
}
