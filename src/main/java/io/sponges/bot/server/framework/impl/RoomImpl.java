package io.sponges.bot.server.framework.impl;

import io.sponges.bot.server.Client;
import io.sponges.bot.server.framework.Network;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.framework.User;
import io.sponges.bot.server.messages.SendRawMessage;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.UserRole;

import java.util.HashMap;
import java.util.Map;

public class RoomImpl implements Room {

    // TODO move internal shit to impl only

    private final Map<String, User> users = new HashMap<>();

    private final Client client;
    private final Network network;
    private final String id;

    // Room details
    private final String topic;

    // data
    private RoomData roomData = null;
    private volatile String prefix = null;

    // TODO implement room data/settings

    protected RoomImpl(Client client, Network network, String id, String topic) {
        this.client = client;
        this.network = network;
        this.id = id;
        this.topic = topic;
    }

    @Override
    public Map<String, User> getUsers() {
        return users;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public RoomData getRoomData() {
        return roomData;
    }

    @Override
    public void setRoomData(RoomData roomData) {
        this.roomData = roomData;
    }

    @Override
    public boolean isUser(String id) {
        return users.containsKey(id);
    }

    @Override
    public User getUser(String id) {
        return users.get(id);
    }

    @Override
    public User getOrCreateUser(String id, String username, String displayName, UserRole role) {
        if (!isUser(id)) {
            return createUser(id, username, displayName, role);
        } else {
            return getUser(id);
        }
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private User createUser(String id, String username, String displayName, UserRole role) {
        User user = new User(client, network, this, id, username, displayName, role);
        users.put(id, user);
        return user;
    }

    @Override
    public void sendMessage(String message) {
        client.sendMessage(new SendRawMessage(client, network.getId(), id, message));
    }
}
