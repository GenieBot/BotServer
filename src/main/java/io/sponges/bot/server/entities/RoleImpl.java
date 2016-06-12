package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Role;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class RoleImpl implements Role {

    private final List<String> permissions = new ArrayList<>();

    private final Storage storage;
    private final String id;
    private final DataObject data;

    public RoleImpl(Storage storage, String id) {
        this.storage = storage;
        this.id = id;
        this.data = new DataObject();
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

    public DataObject getData() {
        if (!data.exists("id")) {
            data.set(storage, "id", id);
        }
        if (data.exists("permissions")) {
            data.set(storage, "permissions", permissions);
        }
        return data;
    }
}
