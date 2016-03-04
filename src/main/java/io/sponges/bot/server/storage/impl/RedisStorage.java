package io.sponges.bot.server.storage.impl;

import io.sponges.bot.server.framework.Network;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.storage.Storage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisStorage implements Storage {

    public static final String HOST = "localhost";
    private static final String ROOM_FORMAT = "%s:%s:%s";

    private final Object lock = new Object();

    private final Jedis jedis;

    RedisStorage() {
        this.jedis = new Jedis(HOST);
    }

    @Override
    public RoomData load(Room room) {
        synchronized (lock) {
            String id = room.getId();
            Network network = room.getNetwork();

            String key = String.format(ROOM_FORMAT, room.getClient().getId(), network.getId(), id);
            if (!jedis.exists(key)) {
                setup(room);
            }

            return loadRoom(room);
        }
    }

    @Override
    public void save(RoomData data) {
        synchronized (lock) {
            Room room = data.getRoom();
            Network network = room.getNetwork();
            String key = String.format(ROOM_FORMAT, room.getClient().getId(), network.getId(), room.getId());
            jedis.set(key, data.toString());
        }
    }

    private JSONObject getJsonData(Room room) {
        Network network = room.getNetwork();
        String key = String.format(ROOM_FORMAT, room.getClient().getId(), network.getId(), room.getId());
        return new JSONObject(jedis.get(key));
    }

    private RoomData loadRoom(Room room) {
        RoomDataImpl roomData = new RoomDataImpl(room);
        JSONObject json = getJsonData(room);
        Map<String, Object> data = new HashMap<>();

        boolean changed = false;
        for (Setting setting : Setting.values()) {
            String name = setting.toString();

            Object object;
            try {
                object = json.get(name);
            } catch (JSONException e) {
                object = setting.getValue();
                changed = true;
            }

            if (setting.isList()) {
                JSONArray array = json.getJSONArray(name);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    list.add(array.getString(i));
                }
                object = list;
            }

            data.put(name, object);
        }

        roomData.setData(data);
        if (changed) save(roomData);
        return roomData;
    }

    private void setup(Room room) {
        room.setRoomData(new RoomDataImpl(room));
        RoomData data = room.getRoomData();
        data.loadDefaults();
        save(room.getRoomData());
    }
}
