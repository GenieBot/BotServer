package io.sponges.bot.server.storage;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.api.storage.data.ChannelData;
import io.sponges.bot.api.storage.data.Data;
import io.sponges.bot.api.storage.data.NetworkData;
import io.sponges.bot.api.storage.data.Setting;
import io.sponges.bot.server.entities.NetworkImpl;
import io.sponges.bot.server.entities.channel.GroupChannelImpl;
import io.sponges.bot.server.entities.channel.PrivateChannelImpl;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Map;
import java.util.function.Consumer;

public class StorageImpl implements Storage {

    public static final String NETWORK_DATA_LOCATION = "%s:data";
    public static final String CHANNEL_DATA_LOCATION = "%s:%s:data";

    private final JedisPool pool;

    public StorageImpl(String host, int port) throws JedisConnectionException {
        this.pool = new JedisPool(new JedisPoolConfig(), host, port);
    }

    @Override
    public void load(Network network, Consumer<NetworkData> consumer) {
        try (Jedis jedis = pool.getResource()) {
            NetworkData data = new NetworkData(network);
            NetworkImpl impl = (NetworkImpl) network;
            impl.setNetworkData(data);
            String dataLocation = String.format(NETWORK_DATA_LOCATION, network.getId());
            if (!jedis.exists(dataLocation)) {
                setDefaults(data);
                save(network, success -> {
                    System.out.println("Successfully saved defaults for " + dataLocation);
                });
            } else {
                String content = jedis.get(dataLocation);
                JSONObject json = new JSONObject(content);
                loadSettings(data, json);
            }
            consumer.accept(data);
        }
    }

    @Override
    public void save(Network network, Consumer<String> consumer) {
        try (Jedis jedis = pool.getResource()) {
            NetworkData data = network.getData();
            String dataLocation = String.format(NETWORK_DATA_LOCATION, network.getId());
            JSONObject settings = serializeSettings(data);
            consumer.accept(jedis.set(dataLocation, new JSONObject().put("settings", settings).toString()));
        }
    }

    @Override
    public void load(Channel channel, Consumer<ChannelData> consumer) {
        try (Jedis jedis = pool.getResource()) {
            ChannelData data = new ChannelData(channel);
            if (channel instanceof GroupChannel) {
                ((GroupChannelImpl) channel).setChannelData(data);
            } else {
                ((PrivateChannelImpl) channel).setChannelData(data);
            }
            Network network = channel.getNetwork();
            String dataLocation = String.format(CHANNEL_DATA_LOCATION, network.getId(), channel.getId());
            if (!jedis.exists(dataLocation)) {
                setDefaults(data);
                save(channel, success -> {
                    System.out.println("Successfully saved defaults for " + dataLocation);
                });
            } else {
                String content = jedis.get(dataLocation);
                JSONObject json = new JSONObject(content);
                loadSettings(data, json);
            }
            consumer.accept(data);
        }
    }

    @Override
    public void save(Channel channel, Consumer<String> consumer) {
        try (Jedis jedis = pool.getResource()) {
            ChannelData data = channel.getData();
            Network network = channel.getNetwork();
            String dataLocation = String.format(CHANNEL_DATA_LOCATION, network.getId(), channel.getId());
            JSONObject settings = serializeSettings(data);
            consumer.accept(jedis.set(dataLocation, new JSONObject().put("settings", settings).toString()));
        }
    }

    @Override
    public boolean isLoaded(Network network) {
        return network.getData() != null;
    }

    @Override
    public boolean isLoaded(Channel channel) {
        return channel.getData() != null;
    }

    private void setDefaults(Data data) {
        String defaultPrefix = null;
        if (data instanceof NetworkData) {
            defaultPrefix = ((NetworkData) data).getNetwork().getClient().getDefaultPrefix();
        } else if (data instanceof ChannelData) {
            defaultPrefix = ((ChannelData) data).getChannel().getNetwork().getClient().getDefaultPrefix();
        }
        data.set(Setting.PREFIX_KEY, defaultPrefix);
    }

    private void loadSettings(Data data, JSONObject json) {
        JSONObject object = json.getJSONObject("settings");
        for (String key : object.keySet()) {
            String value = object.getString(key);
            data.set(key, value);
        }
    }

    private JSONObject serializeSettings(Data data) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> entry : data.getSettings().entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json;
    }

    public JedisPool getPool() {
        return pool;
    }
}
