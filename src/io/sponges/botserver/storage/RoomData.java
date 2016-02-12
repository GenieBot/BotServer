package io.sponges.botserver.storage;

import io.sponges.botserver.framework.Room;
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
