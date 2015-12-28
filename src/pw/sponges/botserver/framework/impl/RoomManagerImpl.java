package pw.sponges.botserver.framework.impl;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.framework.Network;
import pw.sponges.botserver.framework.Room;
import pw.sponges.botserver.framework.RoomManager;

import java.util.HashMap;
import java.util.Map;

public class RoomManagerImpl implements RoomManager {

    private final Client client;
    private final Network network;

    private final Map<String, Room> rooms = new HashMap<>();

    public RoomManagerImpl(Client client, Network network) {
        this.client = client;
        this.network = network;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public Map<String, Room> getRooms() {
        return rooms;
    }

    @Override
    public boolean isRoom(String id) {
        return rooms.containsKey(id);
    }

    @Override
    public Room getRoom(String id) {
        return rooms.get(id);
    }

    @Override
    public Room getOrCreateRoom(String id, String topic) {
        if (!isRoom(id)) {
            return createRoom(id, topic);
        } else {
            return getRoom(id);
        }
    }

    private Room createRoom(String id, String topic) {
        Room room = new Room(client, network, id, topic);
        rooms.put(id, room);
        return room;
    }
}
