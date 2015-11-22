package pw.sponges.botserver.storage.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.RoomGroups;
import pw.sponges.botserver.storage.RoomSettings;

public class RoomDataImpl implements RoomData {

    private RoomSettings settings;
    private RoomGroups groups;

    public RoomDataImpl(RoomSettings settings, RoomGroups groups) {
        this.settings = settings;
        this.groups = groups;
    }

    @Override
    public RoomSettings getSettings() {
        return settings;
    }

    @Override
    public RoomGroups getGroups() {
        return groups;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("settings", settings.getSettings());

        JSONArray array = new JSONArray();
        for (Group group : groups.getGroups()) {
            JSONObject object = new JSONObject();
            object.put("id", group.getId());
            object.put("users", group.getUsers());
            object.put("nodes", group.getPermissionNodes());
            array.put(object);
        }

        json.put("groups", array);
        return json;
    }
}
