package pw.sponges.botserver.permissions.impl;

import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionGroups;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.storage.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public boolean isLoaded(String room) {
        return groups.containsKey(room);
    }

    @Override
    public List<Group> getDefaultGroups(String room) {
        Group defaultGroup = new Group("default");
        defaultGroup.addPermission("command.about");
        defaultGroup.addPermission("command.help");
        defaultGroup.addPermission("command.urban");
        defaultGroup.addPermission("command.chatinfo");
        defaultGroup.addPermission("command.stats");
        defaultGroup.addPermission("command.test");
        defaultGroup.addPermission("command.userinfo");
        defaultGroup.addPermission("command.mcnetworks");
        defaultGroup.addPermission("command.mcserver");
        defaultGroup.addPermission("command.groups");

        Group admin = new Group("admin", "default");
        admin.addPermission("command.link");
        //admin.addPermission("command.groups");

        Group op = new Group("op", "admin");
        op.addPermission("command.op.clients");
        op.addPermission("command.op.joinroom");
        op.addPermission("command.op.stop");

        return new ArrayList<Group>() {{
            add(defaultGroup);
            add(admin);
            add(op);
        }};
    }

    @Override
    public void setGroups(String room, PermissionGroups groups) {
        System.out.println("Set groups for " + room + "!");

        this.groups.put(room, groups);
    }

}
