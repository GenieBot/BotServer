package pw.sponges.botserver.storage.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.storage.*;
import pw.sponges.botserver.util.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONStorage implements Storage {

    private final String FILE_PATH = "./rooms";
    private final String FILE_EXT = ".json";

    private final Database database;

    public JSONStorage(Database database) {
        this.database = database;
    }

    @Override
    public RoomData load(String room) {
        File file = new File(FILE_PATH + "/" + room + FILE_EXT);

        if (file.exists()) return getSettingsFromFile(file);
        else {
            setupFile(file, room);
            return getSettingsFromFile(file);
        }
    }

    @Override
    public void save(String room) {
        JSONObject json = database.getData(room).toJson();
        File file = new File(FILE_PATH + "/" + room + FILE_EXT);
        FileUtils.writeFile(file, json.toString());
    }

    /**
     * Creates the settings file
     * @param file
     */
    private void setupFile(File file, String room) {
        //noinspection ResultOfMethodCallIgnored
        new File(FILE_PATH).mkdirs();
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(file));
            writeDefaults(out, room);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeDefaults(BufferedWriter out, String roomId) throws IOException {
        RoomSettings settings = new RoomSettingsImpl();
        RoomGroups groups = new RoomGroupsImpl();
        RoomData room = new RoomDataImpl(settings, groups);

        List<Group> defaultGroups = room.getGroups().getDefaultGroups(roomId);
        room.getGroups().setValues(defaultGroups);
        room.getGroups().setValues(new HashMap<>());

        Map<Setting, Object> defaultSettings = room.getSettings().getDefaults();
        room.getSettings().setValues(defaultSettings);

        out.write(room.toJson().toString());
    }

    private JSONObject getJsonFile(File file) {
        return new JSONObject(FileUtils.readFile(file));
    }

    private RoomData getSettingsFromFile(File file) {
        JSONObject json = getJsonFile(file);
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
        Map<String, Group> users = new HashMap<>();

        RoomSettings settings = new RoomSettingsImpl();
        RoomGroups groups = new RoomGroupsImpl();
        RoomData room = new RoomDataImpl(settings, groups);

        JSONArray array = json.getJSONArray("groups");
        for (int i = 0; i < array.length(); i++) {
            JSONObject g = array.getJSONObject(i);
            String id = g.getString("id");
            String r = g.getString("room");

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
                users.put(u.getString(x), group);
            }

            groupsList.add(group);
        }

        settings.setValues(data);
        groups.setValues(groupsList);
        groups.setValues(users);

        if (changed) {
            /*FileUtils.writeFile(file, new JSONObject()
                    .put("settings", data)
                    .toString());*/

            FileUtils.writeFile(file, room.toJson().toString());
        }

        return room;
    }

}
