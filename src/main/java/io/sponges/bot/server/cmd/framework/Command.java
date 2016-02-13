package io.sponges.bot.server.cmd.framework;

import io.sponges.bot.server.storage.UserRole;

public abstract class Command {

    private final String permission;
    private final UserRole role;
    private final String description;
    private final String[] names;

    public Command(String permission, UserRole role, String description, String... names) {
        this.permission = permission;
        this.role = role;
        this.description = description;
        this.names = names;
    }

    public String getPermission() {
        return permission;
    }

    public UserRole getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }

    public String[] getNames() {
        return names;
    }

    public abstract void onCommand(CommandRequest request, String[] args);

}
