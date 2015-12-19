package pw.sponges.botserver.storage;

/**
 * Enum storing all the room specific settings
 */
public enum Setting {

    ADMIN_ONLY(false),
    BOT_NAME("SpongyBot"),
    PREFIX("$"),
    SIMPLE_PERMS(true);

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
