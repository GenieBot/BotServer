package pw.sponges.botserver.storage;

import pw.sponges.botserver.permissions.PermissionsManager;

/**
 * Database class for room data
 */
public interface Database {

    /**
     * Returns the PermissionsManager instance
     * @return PermissionsManager instance
     */
    PermissionsManager getPermissions();

    /**
     * Gets the room data from the specified room id
     * @param room the id of the room
     * @return RoomData for the specified room
     */
    RoomData getData(String room);

    /**
     * Loads the data for the specified room from the storage
     * @param room the room to load data from
     * @return RoomData for the specified room
     */
    RoomData load(String room);

    /**
     * Saves the database for the specified room
     * @param room the room to save data for
     */
    void save(String room);

    /**
     * Check if the room has already been loaded
     * @param room the room to check
     * @return if the room has been loaded
     */
    boolean isLoaded(String room);

}
