package io.sponges.bot.server.storage;

import io.sponges.bot.server.framework.Room;
import org.json.JSONObject;

import java.util.Map;

public interface RoomData {

    Room getRoom();

    JSONObject toJson();

    Map<String, Object> getData();

    Object get(String key);

    Object get(Setting key);

    void set(String key, Object value);

    void set(Setting key, Object value);

    void loadDefaults();

}
