package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.data.UserData;
import org.json.JSONObject;

public final class KickUserMessage extends Message {

    private final Network network;
    private final User user;
    private final UserData userData;

    public KickUserMessage(Client client, Network network, User user) {
        super(client, "KICK_USER");
        this.network = network;
        this.user = user;
        this.userData = user.getUserData();
    }

    @Override
    public JSONObject toJson() {
        JSONObject user = new JSONObject();
        user.put("id", this.user.getId());
        if (this.userData.getUsername().isPresent()) {
            user.put("username", this.userData.getUsername().get());
        }
        if (this.userData.getDisplayName().isPresent()) {
            user.put("display-name", this.userData.getDisplayName().get());
        }
        return new JSONObject()
                .put("network", network.getId())
                .put("user", user);
    }
}