package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.server.protocol.msg.KickUserMessage;
import io.sponges.bot.server.protocol.msg.ResourceRequestMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class UserManagerImpl implements UserManager {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    private final Network network;

    public UserManagerImpl(Network network) {
        this.network = network;
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

    @Override
    public void loadUser(String userId, Consumer<User> consumer) {
        if (isUser(userId)) {
            consumer.accept(getUser(userId));
            return;
        }
        new ResourceRequestMessage(network.getClient(), network.getId(), ResourceRequestMessage.ResourceType.USER,
                userId, entity -> {
            User user = (User) entity;
            if (user != null) users.put(user.getId(), user);
            consumer.accept(user);
        }).send();
    }

    @Override
    public User loadUserSync(String s) {
        AtomicBoolean set = new AtomicBoolean(false);
        AtomicReference<User> net = new AtomicReference<>();
        loadUser(s, user -> {
            set.set(true);
            net.set(user);
        });
        while (!set.get()) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return net.get();
    }
}
