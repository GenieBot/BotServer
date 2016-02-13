package io.sponges.bot.server.framework;

import io.sponges.bot.server.messages.SendRawMessage;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.Client;
import io.sponges.bot.server.storage.UserRole;

import java.util.HashMap;
import java.util.Map;

public class Room {

    // TODO move internal shit to impl only

    private final Map<String, User> users = new HashMap<>();

    private final Client client;
    private final Network network;
    private final String id;

    // Room details
    private final String topic;

    // data
    private RoomData roomData = null;

    // TODO implement room data/settings

    public Room(Client client, Network network, String id, String topic) {
        this.client = client;
        this.network = network;
        this.id = id;
        this.topic = topic;
    }

    public Client getClient() {
        return client;
    }

    public Network getNetwork() {
        return network;
    }

    public String getId() {
        return id;
    }

    public RoomData getRoomData() {
        return roomData;
    }

    public void setRoomData(RoomData roomData) {
        this.roomData = roomData;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public boolean isUser(String id) {
        return users.containsKey(id);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public User getOrCreateUser(String id, String username, String displayName, UserRole role) {
        if (!isUser(id)) {
            return createUser(id, username, displayName, role);
        } else {
            return getUser(id);
        }
    }

    private User createUser(String id, String username, String displayName, UserRole role) {
        User user = new User(client, network, this, id, username, displayName, role);
        users.put(id, user);
        return user;
    }

    public String getTopic() {
        return topic;
    }

    public void sendMessage(String message) {
        client.sendMessage(new SendRawMessage(client, network.getId(), id, message));
    }
}
