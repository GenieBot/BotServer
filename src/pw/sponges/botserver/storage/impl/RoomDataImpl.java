package pw.sponges.botserver.storage.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.RoomSettings;
import pw.sponges.botserver.util.Msg;

/**
 * Implementation of the RoomData interface
 */
public class RoomDataImpl implements RoomData {

    private final String room;

    private final RoomSettings settings;
    private final PermissionsManager permissions;

    public RoomDataImpl(String room, RoomSettings settings, PermissionsManager permissions) {
        this.room = room;
        this.settings = settings;
        this.permissions = permissions;
    }

    @Override
    public String getId() {
        return room;
    }

    @Override
    public RoomSettings getSettings() {
        return settings;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("settings", settings.getSettings());

        if (!permissions.isLoaded(room)) {
            Msg.warning("Perms are still not loaded?");
            return json;
        }

        JSONArray array = new JSONArray();
        for (Group group : permissions.getGroups(room).getGroups().values()) {
            JSONObject object = new JSONObject();
            object.put("id", group.getId());
            object.put("users", group.getUsers());
            Msg.debug(group.getId() + " users: " + group.getUsers());
            object.put("nodes", group.getPermissionNodes());

            if (group.getInheritance() != null) {
                object.put("inheritance", group.getInheritance().getId());
            }

            array.put(object);
        }

        json.put("groups", array);
        return json;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }
}
