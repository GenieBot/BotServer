package pw.sponges.botserver;

import pw.sponges.botserver.bridge.BridgeManager;
import pw.sponges.botserver.framework.NetworkManager;
import pw.sponges.botserver.internal.ServerWrapper;
import pw.sponges.botserver.messages.Message;

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

    // TODO javadoc comment for network manager
    NetworkManager getNetworkManager();

}
