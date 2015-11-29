package pw.sponges.botserver.permissions.impl;

import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionGroups;
import pw.sponges.botserver.util.Msg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionGroupsImpl implements PermissionGroups {

    private final Map<String, Group> groups;
    private final Map<String, Group> users;

    public PermissionGroupsImpl() {
        this.groups = new HashMap<>();
        this.users = new HashMap<>();
    }

    @Override
    public Map<String, Group> getGroups() {
        return groups;
    }

    @Override
    public Group getUserGroup(String user) {
        if (!users.containsKey(user)) {
            return setup(user);
        }

        return users.get(user);
    }

    @Override
    public Group getGroup(String group) {
        return groups.get(group);
    }

    @Override
    public void setGroup(String user, Group group) {
        Msg.debug("Setting group for user " + user + " to " + group.getId() + "!");

        if (groups.containsKey(user)) {
            Group old = groups.get(user);
            old.removeUser(user);
            Msg.debug("Removed user " + user + " from group " + old.getId() + "!");
        }

        users.put(user, group);
        group.addUser(user);
        Msg.debug("Set group for user " + user + " to " + group.getId() + "!");
    }

    @Override
    public Group setup(String user) {
        Group group = groups.get("default");
        setGroup(user, group);
        return group;
    }

    @Override
    public void addGroup(Group group) {
        groups.put(group.getId(), group);

        for (String user : group.getUsers()) {
            this.users.put(user, group);
        }
    }

    @Override
    public void removeGroup(String id) {
        groups.remove(id);
    }

    @Override
    public boolean isSetup(String user) {
        return users.containsKey(user);
    }

    @Override
    public void setGroups(List<Group> groups) {
        groups.forEach(this::addGroup);

        for (Group group : this.groups.values()) {
            group.setGroups(this);
        }
    }

    @Override
    public boolean isGroup(String group) {
        return groups.containsKey(group);
    }
}
