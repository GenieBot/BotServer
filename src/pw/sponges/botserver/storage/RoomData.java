package pw.sponges.botserver.storage;

import org.json.JSONObject;

/**
 * Class that contains the data for a room
 */
public interface RoomData {

    /**
     * Gets the ID of the room this data is assigned to
     * @return room id
     */
    String getId();

    /**
     * Gets the settings for the specified room
     * @return RoomSettings instance
     */
    RoomSettings getSettings();

    /**
     * Converts the data for the room to JSON
     * @return JSONObject for room data
     */
    JSONObject toJson();

}
