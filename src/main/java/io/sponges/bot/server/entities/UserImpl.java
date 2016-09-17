package io.sponges.bot.server.entities;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.data.UserData;
import io.sponges.bot.server.entities.data.UserDataImpl;

import java.util.UUID;

public class UserImpl implements User {

    private final UUID id;
    private final String sourceId;
    private final Network network;
    private final boolean platformAdmin, op;
    private final UserData userData;

    public UserImpl(UUID id, String sourceId, Network network, boolean platformAdmin, boolean op) {
        this.id = id;
        this.sourceId = sourceId;
        this.network = network;
        this.platformAdmin = platformAdmin;
        this.op = op;
        this.userData = new UserDataImpl();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getSourceId() {
        return sourceId;
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
    public boolean hasPermission(String s) {
        return platformAdmin;
    }

    @Override
    public void kick() {
        network.getUserManager().kickUser(this);
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    public void sendPrivateMessage(String message) {
        userData.getPrivateChannel().get().sendChatMessage(message);
    }
}
