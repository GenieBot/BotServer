package pw.sponges.botserver.permissions;

public interface PermissionsManager {

    PermissionGroups getGroups(String room);

    void loadPermissions(String room);

    boolean isLoaded(String room);

}
