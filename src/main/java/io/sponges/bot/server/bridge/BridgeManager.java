package io.sponges.bot.server.bridge;

public interface BridgeManager {

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
