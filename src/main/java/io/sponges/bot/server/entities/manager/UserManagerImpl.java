package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.Role;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.manager.RoleManager;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.server.protocol.msg.KickUserMessage;
import io.sponges.bot.server.protocol.msg.ResourceRequestMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class UserManagerImpl implements UserManager {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, String> toAssign = new ConcurrentHashMap<>();

    private final Network network;
    private final RoleManager roleManager;

    public UserManagerImpl(Network network) {
        this.network = network;
        this.roleManager = network.getRoleManager();
    }

    @Override
    public Map<String, User> getUsers() {
        return users;
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
        new KickUserMessage(network.getClient(), network, user).send();
    }

    public void addUser(User user) {
        String userId = user.getId();
        if (toAssign.containsKey(userId)) {
            String roleId = toAssign.get(userId);
            Role role = roleManager.getRole(roleId);
            user.setRole(role);
            toAssign.remove(userId);
        }
        users.put(userId, user);
    }

    public Map<String, String> getToAssign() {
        return toAssign;
    }

    @Override
    public void loadUser(String userId, Consumer<User> consumer) {
        if (isUser(userId)) {
            consumer.accept(getUser(userId));
            return;
        }
        new ResourceRequestMessage(network.getClient(), network.getId(), ResourceRequestMessage.ResourceType.USER,
                userId, entity -> {
            User user = (User) entity;
            users.put(user.getId(), user);
            consumer.accept(user);
        }).send();
    }
}
