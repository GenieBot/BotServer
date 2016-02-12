package io.sponges.botserver.storage;

import java.util.ArrayList;

public enum Setting {

    ADMIN_ONLY(false),
    PREFIX("$"),
    LINK_PARSING(false),
    ASSHOLE_MODE(false),
    ALLOW_COPYPASTA(false),
    DISABLED_COMMANDS(new ArrayList<>(), true),
    BANNED_USERS(new ArrayList<>(), true),
    DEBUG(false),
    SHOW_FULL_JSON(false);

    private final Object value;

    private boolean list = false;

    Setting(Object value) {
        this.value = value;
    }

    Setting(Object value, boolean list) {
        this.value = value;
        this.list = list;
    }

    public Object getValue() {
        return value;
    }

    public boolean isList() {
        return list;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase().replace("_", "-");
    }

}
