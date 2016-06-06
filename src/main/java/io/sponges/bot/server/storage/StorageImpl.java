package io.sponges.bot.server.storage;

import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.Storage;
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
    public Object get(String s) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(s);
        }
    }

    @Override
    public void set(String s, Object o) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(s, String.valueOf(o));
        }
    }

    @Override
    public String serialize(DataObject dataObject) {
        return toJson(dataObject).toString();
    }

    public JSONObject toJson(DataObject object) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Object> entry : object.getMappings().entrySet()) {
            Object value = entry.getValue();
            json.put(entry.getKey(), value instanceof DataObject ? serialize((DataObject) value) : value);
        }
        return json;
    }

    @Override
    public void load(DataObject object) {
        String key = object.getKey();
        if (!exists(key)) {
            set(key, serialize(object));
            return;
        }
        JSONObject json = new JSONObject(get(key));
        load(object, json);
    }

    private DataObject load(String key, JSONObject json) {
        DataObject object = new DataObject(key);
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
