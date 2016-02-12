package io.sponges.botserver.framework.impl;

import io.sponges.botserver.Client;
import io.sponges.botserver.framework.Room;
import io.sponges.botserver.framework.RoomManager;
import io.sponges.botserver.framework.Network;

public class NetworkImpl implements Network {

    private final Client client;
    private final String id;
    private final RoomManager roomManager;

    public NetworkImpl(Client client, String id) {
        this.client = client;
        this.id = id;
        this.roomManager = new RoomManagerImpl(client, this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public RoomManager getRoomManager() {
        return roomManager;
    }

    @Override
    public Room getRoomWithUser(String user) {
        for (Room room : roomManager.getRooms().values()) {
            if (room.isUser(user)) {
                return room;
            }
        }

        return null;
    }

}
