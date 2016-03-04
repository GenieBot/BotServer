package io.sponges.bot.server;

import io.sponges.bot.server.framework.NetworkManager;
import io.sponges.bot.server.messages.Message;

/**
 * Interface for all connected clients
 */
public interface Client {

    /**
     * The ID of the connected client
     * @return client id
     */
    String getId();

    String getChannel();

    /**
     * Send a Message to the client
     * @param message Message to send
     */
    void sendMessage(Message message);

    /**
     * The manager for each network
     * @return NetworkManager instance
     */
    NetworkManager getNetworkManager();

}
