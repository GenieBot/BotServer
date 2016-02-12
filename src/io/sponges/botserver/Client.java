package io.sponges.botserver;

import io.sponges.botserver.bridge.BridgeManager;
import io.sponges.botserver.framework.NetworkManager;
import io.sponges.botserver.internal.ServerWrapper;
import io.sponges.botserver.messages.Message;

/**
 * Interface for all connected clients
 */
public interface Client {

    /**
     * The ID of the connected client
     * @return client id
     */
    String getId();

    /**
     * Send a Message to the client
     * @param message Message to send
     */
    void sendMessage(Message message);

    /**
     * The wrapper for the internal server implementation
     * @return server wrapper
     */
    ServerWrapper getWrapper();

    /**
     * Gets the manager for room bridges
     * @return BridgeManager instance
     */
    BridgeManager getBridgeManager();

    /**
     * The manager for each network
     * @return NetworkManager instance
     */
    NetworkManager getNetworkManager();

}
