package io.sponges.botserver.storage;

import io.sponges.botserver.framework.Room;

public interface Database {

    /**
     * Loads the data for the specified room from the storage
     * @param room the room to load data from
     * @return RoomData for the specified room
     */
    RoomData load(Room room);

    /**
     * Saves the database for the specified room
     * @param room the room to save data for
     */
    void save(Room room);

    /**
     * Check if the room has already been loaded
     * @param room the room to check
     * @return if the room has been loaded
     */
    boolean isLoaded(Room room);

}
