package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.manager.ClientManager;
import io.sponges.bot.server.entities.ClientImpl;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ClientManagerImpl implements ClientManager {

    private final Map<String, Client> clients = new HashMap<>();

    @Override
    public Map<String, Client> getClients() {
        return clients;
    }

    @Override
    public boolean isClient(String s) {
        return clients.containsKey(s);
    }

    @Override
    public Client getClient(String s) {
        return clients.get(s);
    }

    public Client getClient(SocketAddress address) {
        for (Client client : clients.values()) {
            if (((ClientImpl) client).getRemoteAddress() == address) {
                return client;
            }
        }
        return null;
    }

}
