package io.sponges.botserver.framework;

import io.sponges.botserver.Client;
import io.sponges.botserver.storage.UserRole;

public class User {

    private final Client client;
    private final Network network;
    private final Room room;
    private final String id;

    // user details
    private String username;
    private String displayName;
    private UserRole role;

    public User(Client client, Network network, Room room, String id, String username, String displayName, UserRole role) {
        this.client = client;
        this.network = network;
        this.room = room;
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.role = role;
    }

    public Client getClient() {
        return client;
    }

    public Network getNetwork() {
        return network;
    }

    public Room getRoom() {
        return room;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
