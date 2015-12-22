package pw.sponges.botserver.storage;

import java.util.ArrayList;

/**
 * Enum storing all the room specific settings
 */
public enum Setting {

    DEBUG(false),
    ADMIN_ONLY(false),
    PREFIX("$"),
    SIMPLE_PERMS(true),
    LINK_PARSING(false),
    DISABLED_COMMANDS(new ArrayList<>()),
    BANNED_USERS(new ArrayList<>()),
    SHOW_FULL_JSON(false);

    private Object value;

    Setting(Object value) {
        this.value = value;
    }

    /**
     * Gets the value of the Setting
     * @return Object value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Formats the Setting name for storing
     * @return Setting name as a String
     */
    @Override
    public String toString() {
        return this.name().toLowerCase().replace("_", "-");
    }
}
