package io.sponges.bot.server.framework.impl;

import io.sponges.bot.server.Client;
import io.sponges.bot.server.framework.Network;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.framework.RoomManager;

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
