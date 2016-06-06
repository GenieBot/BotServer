package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.Role;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.data.UserData;
import io.sponges.bot.api.storage.DataObject;
import io.sponges.bot.api.storage.Storage;
import io.sponges.bot.server.entities.data.UserDataImpl;
import io.sponges.bot.server.entities.manager.RoleManagerImpl;

import java.util.List;

public class UserImpl implements User {

    private static final String DATA_KEY = "clients:%s:networks:%s:users:%s:data";

    private final String id;
    private final Network network;
    private final boolean platformAdmin, op;

    private final RoleManagerImpl roleManager;
    private final UserData userData;
    private final DataObject data;

    private Role role = null;

    public UserImpl(String id, Network network, boolean platformAdmin, boolean op, Storage storage) {
        this.id = id;
        this.network = network;
        this.platformAdmin = platformAdmin;
        this.op = op;
        this.roleManager = (RoleManagerImpl) network.getRoleManager();
        this.userData = new UserDataImpl();
        this.data = new DataObject(String.format(DATA_KEY, network.getClient().getId(), network.getId(), id));
        storage.load(this.data);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public boolean isPlatformAdmin() {
        return platformAdmin;
    }

    @Override
    public boolean isOp() {
        return op;
    }

    @Override
    public List<Channel> getChannels() {
        return network.getChannelManager().getChannels(this);
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
        this.roleManager.assignRole(this, role);
    }

    @Override
    public boolean hasPermission(String s) {
        return platformAdmin || role != null && role.hasPermission(s);
    }

    @Override
    public void kick() {
        network.getUserManager().kickUser(this);
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    public DataObject getData() {
        return data;
    }
}
