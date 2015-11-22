package pw.sponges.botserver.permissions;

public interface PermissionGroups {

    Group getGroup(String user);

    void setGroup(String user, Group group);

    Group setup(String user);

    void addGroup(Group group);

    void removeGroup(String id);

    boolean isSetup(String user);

}
