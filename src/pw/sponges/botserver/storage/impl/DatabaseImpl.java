package pw.sponges.botserver.storage.impl;

import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.permissions.impl.PermissionsManagerImpl;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.Storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the Database interface
 */
public class DatabaseImpl implements Database {

    private PermissionsManager permissions;

    private Map<String, RoomData> data;
    private Storage storage;

    public DatabaseImpl() {
        this.permissions = new PermissionsManagerImpl(this);
        this.data = new HashMap<>();

        //this.storage = new JSONStorage(this, permissions);
        this.storage = new RedisStorage(permissions);
    }

    @Override
    public RoomData load(String room) {
        RoomData data = storage.load(room);
        this.data.put(room, data);
        return data;
    }

    @Override
    public void save(String room) {
        storage.save(data.get(room));
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
