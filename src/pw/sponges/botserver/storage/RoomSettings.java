package pw.sponges.botserver.storage;

import java.util.Map;

/**
 * Contains all of the settings for a room
 */
public interface RoomSettings {

    /**
     * Gets the Map containing all the room's settings and values
     * @return settings Map
     */
    Map<Setting, Object> getSettings();

    /**
     * Sets the values of the room's settings Map
     * @param settings the new room settings Map
     */
    void setValues(Map<Setting, Object> settings);

    /**
     * Gets the value of a Setting
     * @param setting the setting to get the value of
     * @return the value of the specified setting
     */
    Object get(Setting setting);

    /**
     * Sets the value of a Setting to an Object
     * @param setting the setting to set the value of
     * @param value the value of the setting
     */
    void set(Setting setting, Object value);

    /**
     * Gets the default settings Map for a room if it does not already have settings specified
     * @return default settings Map
     */
    Map<Setting, Object> getDefaults();

}
