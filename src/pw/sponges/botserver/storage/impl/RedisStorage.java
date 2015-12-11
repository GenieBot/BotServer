package pw.sponges.botserver.storage.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionGroups;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.permissions.impl.PermissionGroupsImpl;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.RoomSettings;
import pw.sponges.botserver.storage.Setting;
import pw.sponges.botserver.storage.Storage;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisStorage implements Storage {

    public static final String HOST = "localhost";
    public static final String FORMAT = "room:%s";

    private final PermissionsManager permissions;

    private final Jedis jedis;

    public RedisStorage(PermissionsManager permissions) {
        this.permissions = permissions;

        jedis = new Jedis(HOST);
    }

    @Override
    public RoomData load(String room) {
        if (!jedis.exists(String.format(FORMAT, room))) {
            setup(room);
        }

        return loadRoom(room);
    }

    @Override
    public void save(RoomData data) {
        jedis.set(String.format(FORMAT, data.getId()), data.toString());
    }

    private void setup(String roomId) {
        RoomSettings settings = new RoomSettingsImpl();
        RoomData room = new RoomDataImpl(roomId, settings, permissions);

        PermissionGroups groups = new PermissionGroupsImpl();
        List<Group> defaultGroups = permissions.getDefaultGroups(roomId);
        groups.setGroups(defaultGroups);
        permissions.setGroups(roomId, groups);

        Map<Setting, Object> defaultSettings = room.getSettings().getDefaults();
        room.getSettings().setValues(defaultSettings);

        jedis.set(String.format(FORMAT, roomId), room.toString());
    }

    private JSONObject getData(String room) {
        return new JSONObject(jedis.get(String.format(FORMAT, room)));
    }

    private RoomData loadRoom(String roomId) {
        JSONObject json = getData(roomId);
        JSONObject storedSettings = json.getJSONObject("settings");
        Map<Setting, Object> data = new HashMap<>();

        boolean changed = false;

        for (Setting setting : Setting.values()) {
            String name = setting.toString();
            Object obj;

            try {
                obj = storedSettings.get(name);
            } catch (JSONException e) {
                obj = setting.getObject();
                changed = true;
            }

            data.put(setting, obj);
        }

        List<Group> groupsList = new ArrayList<>();

        RoomSettings settings = new RoomSettingsImpl();
        RoomData room = new RoomDataImpl(roomId, settings, permissions);

        PermissionGroups groups = new PermissionGroupsImpl();

        JSONArray array = json.getJSONArray("groups");
        for (int i = 0; i < array.length(); i++) {
            JSONObject g = array.getJSONObject(i);
            String id = g.getString("id");

            Group group;

            if (g.isNull("inheritance")) {
                group = new Group(id);
            } else {
                String inheritance = g.getString("inheritance");
                group = new Group(id, inheritance);
            }

            JSONArray nodes = g.getJSONArray("nodes");
            for (int x = 0; x < nodes.length(); x++) {
                group.getPermissionNodes().add(nodes.getString(x));
            }

            JSONArray u = g.getJSONArray("users");
            for (int x = 0; x < u.length(); x++) {
                group.addUser(u.getString(x));
            }

            groupsList.add(group);
        }

        settings.setValues(data);
        groups.setGroups(groupsList);
        permissions.setGroups(roomId, groups);

        if (changed) {
            save(room);
        }

        return room;
    }

}
