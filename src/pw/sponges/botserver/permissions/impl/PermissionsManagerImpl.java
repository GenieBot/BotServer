package pw.sponges.botserver.permissions.impl;

import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.permissions.PermissionGroups;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.RoomGroups;

import java.util.HashMap;
import java.util.Map;

public class PermissionsManagerImpl implements PermissionsManager {

    private final Database database;
    private final Map<String, PermissionGroups> groups;

    public PermissionsManagerImpl(Database database) {
        this.database = database;
        this.groups = new HashMap<>();
    }

    @Override
    public PermissionGroups getGroups(String room) {
        return groups.get(room);
    }

    @Override
    public void loadPermissions(String room) {
        RoomData data = database.getData(room);
        RoomGroups groups = data.getGroups();

        PermissionGroups permissionGroups = new PermissionGroupsImpl();
        groups.getGroups().forEach(permissionGroups::addGroup);

        Map<String, Group> users = groups.getUsers();
        for (String user : users.keySet()) {
            permissionGroups.setGroup(user, users.get(user));
        }

        this.groups.put(room, permissionGroups);
    }

    @Override
    public boolean isLoaded(String room) {
        return groups.containsKey(room);
    }
}
