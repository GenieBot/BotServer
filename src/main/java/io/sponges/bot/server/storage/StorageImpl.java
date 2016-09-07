package io.sponges.bot.server.storage;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.ModuleDataObject;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.Bot;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public class StorageImpl implements Storage {

    private final JedisPool pool;

    public StorageImpl(String host, int port) {
        this.pool = new JedisPool(new JedisPoolConfig(), host, port);
    }

    @Override
    public boolean exists(String s) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(s);
        }
    }

    @Override
    public String get(String s) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(s);
        }
    }

    @Override
    public void set(String s, Object o) {
        Bot.LOGGER.log(Logger.Type.DEBUG, "setting " + s + " to " + String.valueOf(o));
        try (Jedis jedis = pool.getResource()) {
            jedis.set(s, String.valueOf(o));
        }
    }

    @Override
    public String serialize(DataObject dataObject) {
        return toJson(dataObject).toString();
    }

    private JSONObject toJson(DataObject object) {
        JSONObject json = new JSONObject();
        if (object instanceof ModuleDataObject) {
            json.put(ModuleDataObject.MODULE_DATA_OBJECT_IDENTIFIER, true);
        }
        for (Map.Entry<String, Object> entry : object.getMappings().entrySet()) {
            Object value = entry.getValue();
            json.put(entry.getKey(), value instanceof DataObject ? toJson((DataObject) value) : value);
        }
        return json;
    }

    @Override
    public void load(DataObject object) {
        String key = object.getKey();
        if (!exists(key)) {
            String serial = serialize(object);
            set(key, serial);
            return;
        }
        String val = get(key);
        JSONObject json = new JSONObject(val);
        load(object, json);
    }

    @Override
    public void save(DataObject dataObject) {
        String value = serialize(dataObject);
        Bot.LOGGER.log(Logger.Type.DEBUG, "value: " + value);
        set(dataObject.getKey(), value);
    }

    private DataObject load(String key, JSONObject json) {
        DataObject object;
        if (!json.isNull(ModuleDataObject.MODULE_DATA_OBJECT_IDENTIFIER)) {
            object = new ModuleDataObject(key);
        } else {
            object = new DataObject(key);
        }
        load(object, json);
        return object;
    }

    private void load(DataObject object, JSONObject json) {
        Map<String, Object> mappings = object.getMappings();
        load(mappings, json);
    }

    private void load(Map<String, Object> mappings, JSONObject json) {
        for (String s : json.keySet()) {
            Object value = json.get(s);
            if (value instanceof JSONObject) {
                mappings.put(s, load(s, (JSONObject) value));
            } else {
                mappings.put(s, value);
            }
        }
    }

    public JedisPool getPool() {
        return pool;
    }
}
