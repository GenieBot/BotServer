package io.sponges.botserver.framework;

import java.util.Map;

public interface RoomManager {

    Network getNetwork();

    Map<String, Room> getRooms();

    boolean isRoom(String id);

    Room getRoom(String id);

    Room getOrCreateRoom(String id, String topic);

}
