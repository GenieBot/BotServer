package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.Role;
import io.sponges.bot.api.entities.manager.RoleManager;
import io.sponges.bot.server.entities.RoleImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RoleManagerImpl implements RoleManager {

    private final Map<String, Role> roles = new HashMap<>();

    private final Network network;

    public RoleManagerImpl(Network network) {
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

    public void registerRole(Role role) {
        roles.put(role.getId(), role);
    }
}
