package io.sponges.bot.server.storage;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.Role;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.manager.RoleManager;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.api.storage.data.ChannelData;
import io.sponges.bot.api.storage.data.Data;
import io.sponges.bot.api.storage.data.NetworkData;
import io.sponges.bot.api.storage.data.Setting;
import io.sponges.bot.server.entities.NetworkImpl;
import io.sponges.bot.server.entities.RoleImpl;
import io.sponges.bot.server.entities.channel.GroupChannelImpl;
import io.sponges.bot.server.entities.channel.PrivateChannelImpl;
import io.sponges.bot.server.entities.manager.RoleManagerImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class StorageImpl implements Storage {

    public static final String NETWORK_SETTINGS_LOCATION = "%s:%s:settings";
    public static final String CHANNEL_SETTINGS_LOCATION = "%s:%s:%s:settings";

    public static final String NETWORK_PERMISSIONS_LOCATION = "%s:%s:roles";

    private final JedisPool pool;

    public StorageImpl(String host, int port) throws JedisConnectionException {
        this.pool = new JedisPool(new JedisPoolConfig(), host, port);
    }

    @Override
    public void load(Network network, Consumer<NetworkData> consumer) {
        try (Jedis jedis = pool.getResource()) {
            Client client = network.getClient();
            NetworkData data = new NetworkData(network);
            NetworkImpl impl = (NetworkImpl) network;
            impl.setNetworkData(data);
            String dataLocation = String.format(NETWORK_SETTINGS_LOCATION, client.getId(), network.getId());
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
            String permissionsLocation = String.format(NETWORK_PERMISSIONS_LOCATION, client.getId(), network.getId());
            if (jedis.exists(permissionsLocation)) {
                String content = jedis.get(permissionsLocation);
                JSONObject json = new JSONObject(content);
                loadPermissions(network, json);
            }
            consumer.accept(data);
        }
    }

    @Override
    public void save(Network network, Consumer<String> consumer) {
        try (Jedis jedis = pool.getResource()) {
            Client client = network.getClient();
            NetworkData data = network.getData();
            String dataLocation = String.format(NETWORK_SETTINGS_LOCATION, client.getId(), network.getId());
            String permissionsLocation = String.format(NETWORK_PERMISSIONS_LOCATION, client.getId(), network.getId());
            JSONObject settings = serializeSettings(data);
            JSONObject permissions = serializePermissions(network);
            String settingsResponse = jedis.set(dataLocation, new JSONObject().put("settings", settings).toString());
            String permissionsResponse = jedis.set(permissionsLocation, new JSONObject().put("permissions", permissions).toString());
            consumer.accept("Settings response: " + settingsResponse + System.lineSeparator()
                    + "Permissions response: " + permissionsResponse);
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
            Client client = network.getClient();
            String dataLocation = String.format(CHANNEL_SETTINGS_LOCATION, client.getId(), network.getId(), channel.getId());
            if (!jedis.exists(dataLocation)) {
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
            Client client = network.getClient();
            String dataLocation = String.format(CHANNEL_SETTINGS_LOCATION, client.getId(), network.getId(), channel.getId());
            JSONObject settings = serializeSettings(data);
            consumer.accept(jedis.set(dataLocation, new JSONObject().put("settings", settings).toString()));
        }
    }

    @Override
    public String get(String s) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(s);
        }
    }

    @Override
    public void set(String s, String s1, Consumer<String> consumer) {
        try (Jedis jedis = pool.getResource()) {
            String response = jedis.set(s, s1);
            consumer.accept(response);
        }
    }

    @Override
    public boolean isPresent(String s) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(s);
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

    private void setDefaults(NetworkData data) {
        String defaultPrefix = data.getNetwork().getClient().getDefaultPrefix();
        data.set(Setting.PREFIX_KEY, defaultPrefix);
    }

    private void loadPermissions(Network network, JSONObject json) {
        JSONObject object = json.getJSONObject("permissions");
        {
            JSONArray array = object.getJSONArray("roles");
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                String roleId = item.getString("id");
                Role role = new RoleImpl(roleId);
                if (!item.isNull("name")) {
                    role.setName(item.getString("name"));
                }
                if (!item.isNull("permissions")) {
                    JSONArray permissionsArray = item.getJSONArray("permissions");
                    for (int x = 0; x < permissionsArray.length(); x++) {
                        role.addPermission(permissionsArray.getString(x));
                    }
                }
                RoleManagerImpl roleManager = (RoleManagerImpl) network.getRoleManager();
                roleManager.registerRole(role);
            }
        }
        {
            JSONArray usersArray = object.getJSONArray("users");
            NetworkImpl networkImpl = (NetworkImpl) network;
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject item = usersArray.getJSONObject(i);
                String userId = item.getString("id");
                String roleId = item.getString("role");
                networkImpl.getToAssign().put(roleId, userId);
            }
        }
    }

    private void loadSettings(Data data, JSONObject json) {
        JSONObject object = json.getJSONObject("settings");
        for (String key : object.keySet()) {
            String value = object.getString(key);
            data.set(key, value);
        }
    }

    private JSONObject serializePermissions(Network network) {
        RoleManager roleManager = network.getRoleManager();
        JSONArray rolesArray = new JSONArray();
        for (Role role : roleManager.getRoles()) {
            JSONObject json = new JSONObject();
            json.put("id", role.getId());
            Optional<String> name = role.getName();
            if (name.isPresent()) json.put("name", name.get());
            json.put("permissions", role.getPermissions());
            rolesArray.put(json);
        }
        JSONArray usersArray = new JSONArray();
        for (User user : network.getUsers().values()) {
            if (user.getRole() == null) continue;
            Role role = user.getRole();
            JSONObject json = new JSONObject();
            json.put("id", user.getId());
            json.put("role", role.getId());
            usersArray.put(json);
        }
        return new JSONObject()
                .put("roles", rolesArray)
                .put("users", usersArray);
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
