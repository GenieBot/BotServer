package pw.sponges.botserver.storage;

/**
 * Interface for all storage implementations, keeping each implementation simple
 */
public interface Storage {

    /**
     * Loads the data for the specified room
     * @param room the room to load data for
     * @return RoomData instance
     */
    RoomData load(String room);

    /**
     * Saves the data for the specified room
     * @param room the room to save data for
     */
    void save(RoomData data);

}
