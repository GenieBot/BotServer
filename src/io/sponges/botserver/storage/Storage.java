package io.sponges.botserver.storage;

import io.sponges.botserver.framework.Room;

public interface Storage {

    /**
     * Loads the data for the specified room
     * @param room the room to load data for
     * @return RoomData instance
     */
    RoomData load(Room room);

    /**
     * Saves the data for the specified room
     * @param data the data to save
     */
    void save(RoomData data);

}
