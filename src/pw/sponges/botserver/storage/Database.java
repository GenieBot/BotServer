package pw.sponges.botserver.storage;

import pw.sponges.botserver.permissions.PermissionsManager;

public interface Database {

    PermissionsManager getPermissions();

    RoomData getData(String room);

    RoomData load(String room);

    void save(String room);

    boolean isLoaded(String room);

}
