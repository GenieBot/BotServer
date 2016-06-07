package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.Role;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.manager.RoleManager;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.entities.RoleImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoleManagerImpl implements RoleManager {

    private final Map<String, Role> roles = new HashMap<>();
    private final Map<Role, List<User>> users = new ConcurrentHashMap<>();

    private final Storage storage;
    private final Network network;

    public RoleManagerImpl(Storage storage, Network network) {
        this.storage = storage;
        this.network = network;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public Collection<Role> getRoles() {
        return Collections.unmodifiableCollection(roles.values());
    }

    @Override
    public boolean isRole(String s) {
        return roles.containsKey(s);
    }

    @Override
    public Role createRole(String id) {
        Role role = new RoleImpl(id);
        roles.put(id, role);
        return role;
    }

    @Override
    public Role getRole(String id) {
        return roles.get(id);
    }

    @Override
    public void removeRole(String id) {
        roles.remove(id);
    }

    @Override
    public boolean hasUsers(Role role) {
        return users.containsKey(role) && !users.get(role).isEmpty();
    }

    @Override
    public Collection<User> getUsersWithRole(Role role) {
        return Collections.unmodifiableCollection(users.get(role));
    }

    public void registerRole(Role role) {
        roles.put(role.getId(), role);
    }

    public void assignRole(User user, Role role) {
        this.users.entrySet().stream().filter(users -> users.getValue().contains(user)).forEach(users -> {
            users.getValue().remove(user);
            this.users.put(users.getKey(), users.getValue());
        });
        List<User> users;
        if (this.users.containsKey(role)) {
            users = this.users.get(role);
        } else {
            users = new ArrayList<>();
        }
        users.add(user);
        this.users.put(role, users);
    }
}
