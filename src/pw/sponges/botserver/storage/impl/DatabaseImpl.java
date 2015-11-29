package pw.sponges.botserver.storage.impl;

import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.permissions.impl.PermissionsManagerImpl;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.RoomData;

import java.util.HashMap;
import java.util.Map;

public class DatabaseImpl implements Database {

    private PermissionsManager permissions;

    private Map<String, RoomData> data;
    private JSONStorage storage;

    public DatabaseImpl() {
        this.permissions = new PermissionsManagerImpl(this);
        this.data = new HashMap<>();
        this.storage = new JSONStorage(this, permissions);
    }

    @Override
    public RoomData load(String room) {
        RoomData data = storage.load(room);
        this.data.put(room, data);
        return data;
    }

    @Override
    public void save(String room) {
        storage.save(room);
    }

    @Override
    public boolean isLoaded(String room) {
        return data.containsKey(room);
    }

    @Override
    public PermissionsManager getPermissions() {
        return permissions;
    }

    @Override
    public RoomData getData(String room) {
        return data.get(room);
    }

}
