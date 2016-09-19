package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.manager.UserManager;
import io.sponges.bot.server.database.Database;
import io.sponges.bot.server.database.statement.select.SelectUserStatement;
import io.sponges.bot.server.protocol.msg.KickUserMessage;
import io.sponges.bot.server.protocol.msg.ResourceRequestMessage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class UserManagerImpl implements UserManager {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    private final Database database;
    private final Network network;

    public UserManagerImpl(Database database, Network network) {
        this.database = database;
        this.network = network;
    }

    @Override
    public Map<String, User> getUsers() {
        return users;
    }

    @Override
    public boolean isUser(UUID s) {
        return users.containsKey(s.toString());
    }

    @Override
    public User getUser(UUID s) {
        return users.get(s.toString());
    }

    @Override
    public void kickUser(User user) {
        new KickUserMessage(network.getClient(), network, user).send();
    }

    private User getUserBySourceId(String sourceId) {
        for (User user : users.values()) {
            if (user.getSourceId().equals(sourceId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void loadUser(String sourceId, Consumer<User> consumer) {
        User user = getUserBySourceId(sourceId);
        if (user != null) {
            consumer.accept(user);
            return;
        }
        new ResourceRequestMessage<User>(network.getClient(), network, ResourceRequestMessage.ResourceType.USER,
                sourceId, u -> {
            if (u != null) {
                users.put(u.getId().toString(), u);
            }
            consumer.accept(u);
        }).send();
    }

    @Override
    public User loadUserSync(String sourceId) {
        AtomicBoolean set = new AtomicBoolean(false);
        AtomicReference<User> net = new AtomicReference<>();
        loadUser(sourceId, user -> {
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

    @Override
    public User loadUserSync(UUID id) {
        String[] results;
        try {
            results = new SelectUserStatement(database, id).executeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String sourceId = results[1];
        return loadUserSync(sourceId);
    }
}
