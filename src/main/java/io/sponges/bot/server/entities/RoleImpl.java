package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleImpl implements Role {

    private final List<String> permissions = new ArrayList<>();

    private final String id;

    public RoleImpl(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<String> getPermissions() {
        return permissions;
    }

    @Override
    public void addPermission(String s) {
        permissions.add(s);
    }

    @Override
    public void removePermission(String s) {
        permissions.remove(s);
    }

    @Override
    public boolean hasPermission(String s) {
        return permissions.contains(s);
    }

}
