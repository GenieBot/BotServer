package pw.sponges.botserver.permissions;

import pw.sponges.botserver.permissions.simple.UserRole;

import java.util.List;
import java.util.Map;

public interface PermissionGroups {

    Map<String, Group> getGroups();

    Group getGroup(String user);

    void setGroup(String user, Group group);

    Group setup(String user);

    Group setup(String user, UserRole role);

    void addGroup(Group group);

    void removeGroup(String id);

    boolean isSetup(String user);

    void setGroups(List<Group> groups);

    boolean isGroup(String group);

    Group getUserGroup(String user);

}
