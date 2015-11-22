package pw.sponges.botserver.permissions.impl;

import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionGroups;

import java.util.HashMap;
import java.util.Map;

public class PermissionGroupsImpl implements PermissionGroups {

    private final String DEFAULT = "default";

    private final Map<String, Group> groups;
    private final Map<String, Group> users;

    public PermissionGroupsImpl() {
        this.groups = new HashMap<>();
        this.users = new HashMap<>();
    }

    @Override
    public Group getGroup(String user) {
        if (!users.containsKey(user)) {
            return setup(user);
        }

        return users.get(user);
    }

    @Override
    public void setGroup(String user, Group group) {
        users.put(user, group);
    }

    @Override
    public Group setup(String user) {
        Group group = groups.get(DEFAULT);
        setGroup(user, group);
        return group;
    }

    @Override
    public void addGroup(Group group) {
        group.setGroups(this);
        groups.put(group.getId(), group);
    }

    @Override
    public void removeGroup(String id) {
        groups.remove(id);
    }

    @Override
    public boolean isSetup(String user) {
        return users.containsKey(user);
    }
}
