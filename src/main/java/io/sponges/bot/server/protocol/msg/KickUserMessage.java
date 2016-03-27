package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import org.json.JSONObject;

public final class KickUserMessage extends Message {

    private final Network network;
    private final User user;

    public KickUserMessage(Client client, Network network, User user) {
        super(client, "KICK_USER");
        this.network = network;
        this.user = user;
    }

    @Override
    public JSONObject toJson() {
        JSONObject user = new JSONObject();
        user.put("id", this.user.getId());
        if (this.user.getUsername().isPresent()) {
            user.put("username", this.user.getUsername().get());
        }
        if (this.user.getDisplayName().isPresent()) {
            user.put("display-name", this.user.getDisplayName().get());
        }
        return new JSONObject()
                .put("network", network.getId())
                .put("user", user);
    }
}
