package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import org.json.JSONObject;

public final class CmdResponseMessage extends Message {

    private final Network network;
    private final Channel channel;
    private final User user;
    private final String response;

    public CmdResponseMessage(Client client, Network network, Channel channel, User user, String response) {
        super(client, "COMMAND_RESPONSE");
        this.network = network;
        this.channel = channel;
        this.user = user;
        this.response = response;
    }

    @Override
    public JSONObject toJson() {
        JSONObject user = new JSONObject();
        user.put("id", this.user.getId());
        user.putOpt("username", this.user.getUsername().get());
        user.putOpt("display-name", this.user.getDisplayName().get());

        return new JSONObject()
                .put("network", network.getId())
                .put("channel", channel.getId())
                .put("user", user)
                .put("response", response);
    }
}
