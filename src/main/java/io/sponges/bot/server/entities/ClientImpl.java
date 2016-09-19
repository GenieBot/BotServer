package io.sponges.bot.server.entities;

import io.netty.channel.Channel;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.data.ClientData;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.entities.data.ClientDataImpl;
import io.sponges.bot.server.entities.manager.NetworkManagerImpl;

import java.net.SocketAddress;
import java.util.UUID;

/**
 * Implementation of the Client interface
 */
public class ClientImpl implements Client {

    private final UUID id;
    private final String sourceId;
    private final String defaultPrefix;
    private final Channel channel;
    private final NetworkManager networkManager;
    private final ClientData clientData;

    public ClientImpl(Database database, UUID id, String sourceId, String defaultPrefix, Channel channel) {
        this.id = id;
        this.sourceId = sourceId;
        this.defaultPrefix = defaultPrefix;
        this.channel = channel;
        this.networkManager = new NetworkManagerImpl(database, this);
        this.clientData = new ClientDataImpl();
    }

    public void write(String message) {
        channel.writeAndFlush(message + "\r\n");
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
    public String getDefaultPrefix() {
        return defaultPrefix;
    }


    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public ClientData getClientData() {
        return clientData;
    }

    public SocketAddress getRemoteAddress() {
        return channel.remoteAddress();
    }
}
