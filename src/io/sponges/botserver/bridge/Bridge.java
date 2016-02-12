package io.sponges.botserver.bridge;

/**
 * POJO for bridged rooms
 */
public class Bridge {

    private final String client, clientRoom, targetClient, targetRoom;

    public Bridge(String client, String clientRoom, String targetClient, String targetRoom) {
        this.client = client;
        this.clientRoom = clientRoom;
        this.targetClient = targetClient;
        this.targetRoom = targetRoom;
    }

    public String getClient() {
        return client;
    }

    public String getClientRoom() {
        return clientRoom;
    }

    public String getTargetClient() {
        return targetClient;
    }

    public String getTargetRoom() {
        return targetRoom;
    }
}
