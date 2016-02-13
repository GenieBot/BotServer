package io.sponges.bot.server.storage.impl;

import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.util.Msg;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RoomDataImpl implements RoomData {

    private final Room room;

    private Map<String, Object> data = null;

    RoomDataImpl(Room room) {
        this.room = room;
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject(data);
        return json;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public Object get(Setting key) {
        return get(key.toString());
    }

    @Override
    public void set(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public void set(Setting key, Object value) {
        set(key.toString(), value);
    }

    @Override
    public void loadDefaults() {
        Msg.debug("Defaults...");
        Map<String, Object> data = new HashMap<>();
        Msg.debug("Defaults... 2");

        for (Setting setting : Setting.values()) {
            Msg.debug("Defaults... 3 " + setting.name().toLowerCase() + " " + setting.getValue());
            data.put(setting.toString(), setting.getValue());
            Msg.debug("Defaults... 4");
        }
        Msg.debug("Defaults... 5");

        this.data = data;
        Msg.debug("Defaults... 6");
    }

    @Override
    public String toString() {
        return toJson().toString();
    }
}
