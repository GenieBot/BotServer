package io.sponges.bot.server.storage;

import io.sponges.bot.server.framework.Room;

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
