package pw.sponges.botserver.permissions;

import pw.sponges.botserver.util.Msg;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private final String id;
    private final List<String> permissionNodes;
    private final List<String> users;
    private final String toInherit;
    private Group inheritance;

    public Group(String id) {
        this.id = id;
        this.permissionNodes = new ArrayList<>();
        this.users = new ArrayList<>();
        this.toInherit = null;
        this.inheritance = null;
    }

    public Group(String id, String toInherit) {
        this.id = id;
        this.permissionNodes = new ArrayList<>();
        this.users = new ArrayList<>();
        this.toInherit = toInherit;
        this.inheritance = null;
    }

    public void setGroups(PermissionGroups groups) {
        Msg.debug("setupGroups(PermissionGroups groups)");
        if (this.toInherit != null) {
            Msg.debug("setupGroups(PermissionGroups groups) 2 group: " + this.getId() + " toInherit: " + toInherit);
            Msg.debug("exists? " + groups.isGroup(toInherit));
            this.inheritance = groups.getGroup(toInherit);
            Msg.debug("setupGroups(PermissionGroups groups) 3 " + this.inheritance.getId());
        }
    }

    public String getId() {
        return id;
    }

    public List<String> getPermissionNodes() {
        return permissionNodes;
    }

    public List<String> getUsers() {
        return users;
    }

    public boolean isInGroup(String user) {
        return user.contains(user);
    }

    public void addUser(String user) {
        users.add(user);
    }

    public void removeUser(String user) {
        users.remove(user);
    }

    public boolean hasPermission(String node) {
        return permissionNodes.contains(node) || inheritance != null && inheritance.hasPermission(node);
    }

    public void addPermission(String node) {
        permissionNodes.add(node);
    }

    public void removePermission(String node) {
        permissionNodes.remove(node);
    }

    public Group getInheritance() {
        return inheritance;
    }
}
