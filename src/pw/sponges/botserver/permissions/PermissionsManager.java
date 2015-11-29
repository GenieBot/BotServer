package pw.sponges.botserver.permissions;

import java.util.List;

public interface PermissionsManager {

    PermissionGroups getGroups(String room);

    boolean isLoaded(String room);

    List<Group> getDefaultGroups(String room);

    void setGroups(String room, PermissionGroups groups);

}
