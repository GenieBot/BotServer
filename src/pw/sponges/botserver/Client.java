package pw.sponges.botserver;

import pw.sponges.botserver.internal.ServerWrapper;
import pw.sponges.botserver.messages.Message;

/**
 * Interface for all connected clients
 * TODO move bridge stuff to BridgeManager
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
     * Checks if the specified room already has a chat bridge
     * @param room room to check
     * @return if room is bridged
     */
    boolean isBridged(String room);

    /**
     * Get the bridge instance
     * @param room room to get 
     * @return
     */
    Bridge getBridge(String room);

    /**
     * Add a bridge instance
     * @param bridge the bridge to add
     */
    void addBridge(Bridge bridge);

    /**
     * Remove a bridge instance
     * @param bridge the bridge to remove
     */
    void removeBridge(Bridge bridge);

}
