package pw.sponges.botserver.storage;

/**
 * Enum storing all the room specific settings
 */
public enum Setting {

    ADMIN_ONLY(false),
    BOT_NAME("SpongyBot"),
    PREFIX("$"),
    AUTO_REPLY("fuck off you bad memer");

    private Object object;

    Setting(Object object) {
        this.object = object;
    }

    /**
     * Gets the value of the Setting
     * TODO change method name to #getValue
     * @return Object value
     */
    public Object getObject() {
        return object;
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
