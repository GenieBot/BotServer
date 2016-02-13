package io.sponges.bot.server.storage.impl;

import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.storage.Storage;
import io.sponges.bot.server.util.Msg;
import io.sponges.bot.server.framework.Network;
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

    private static final String NETWORK_FORMAT = "network:%s:data";
    private static final String ROOM_FORMAT = "network:%s:%s";

    private final Jedis jedis;

    RedisStorage() {
        this.jedis = new Jedis(HOST);
    }

    @Override
    public RoomData load(Room room) {
        Msg.debug("Loading... " + room.getId());
        String id = room.getId();
        Msg.debug("Loading... 2 " + id);
        Network network = room.getNetwork();
        Msg.debug("Loading... 3 " + network.getId());

        String key = String.format(ROOM_FORMAT, network.getId(), id);
        Msg.debug("Loading... 4 " + key);

        if (!jedis.exists(key)) {
            Msg.debug("Loading... 5");
            setup(room);
            Msg.debug("Loading... 6");
        }
        Msg.debug("Loading... 7");

        return loadRoom(room);
    }

    @Override
    public void save(RoomData data) {
        Room room = data.getRoom();
        Network network = room.getNetwork();

        String key = String.format(ROOM_FORMAT, network.getId(), room.getId());
        jedis.set(key, data.toString());
    }

    private JSONObject getJsonData(Room room) {
        Network network = room.getNetwork();
        String key = String.format(ROOM_FORMAT, network.getId(), room.getId());
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
        Msg.debug("Setup...");
        room.setRoomData(new RoomDataImpl(room));
        RoomData data = room.getRoomData();
        Msg.debug("Setup... 1");
        if (room == null) {
            Msg.debug("Setup... 1.1 - Room is null");
        }
        if (data == null) {
            Msg.debug("Setup... 1.5 - Data is null!");
        }
        data.loadDefaults();
        Msg.debug("Setup... 2 " + room.getRoomData().getData());
        save(room.getRoomData());
        Msg.debug("Setup... 3 ");
    }
}
