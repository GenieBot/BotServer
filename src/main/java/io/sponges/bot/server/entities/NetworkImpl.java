package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.Role;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.manager.ChannelManager;
import io.sponges.bot.api.entities.manager.RoleManager;
import io.sponges.bot.api.storage.data.NetworkData;
import io.sponges.bot.server.entities.manager.ChannelManagerImpl;
import io.sponges.bot.server.entities.manager.RoleManagerImpl;
import io.sponges.bot.server.protocol.msg.KickUserMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkImpl implements Network {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, String> toAssign = new ConcurrentHashMap<>();

    private final String id;
    private final Client client;
    private final ChannelManager channelManager;
    private final RoleManager roleManager;

    private NetworkData networkData = null;

    public NetworkImpl(String id, Client client) {
        this.id = id;
        this.client = client;
        this.channelManager = new ChannelManagerImpl(this);
        this.roleManager = new RoleManagerImpl(this);
    }

    @Override
    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        String userId = user.getId();
        if (toAssign.containsKey(userId)) {
            String roleId = toAssign.get(userId);
            RoleManager roleManager = getRoleManager();
            Role role = roleManager.getRole(roleId);
            user.setRole(role);
            toAssign.remove(userId);
        }
        users.put(userId, user);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public NetworkData getData() {
        return networkData;
    }

    public void setNetworkData(NetworkData networkData) {
        this.networkData = networkData;
    }

    @Override
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    @Override
    public RoleManager getRoleManager() {
        return roleManager;
    }

    @Override
    public boolean isUser(String s) {
        return users.containsKey(s);
    }

    @Override
    public User getUser(String s) {
        return users.get(s);
    }

    @Override
    public void kickUser(User user) {
        KickUserMessage message = new KickUserMessage(client, this, user);
        message.send((ClientImpl) client);
    }

    @Override
    public void kickUser(String s) {
        kickUser(users.get(s));
    }

    public Map<String, String> getToAssign() {
        return toAssign;
    }
}
