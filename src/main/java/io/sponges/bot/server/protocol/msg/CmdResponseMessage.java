package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.PrivateChannel;
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
        JSONObject channel = new JSONObject();
        channel.put("id", this.channel.getId());
        channel.put("private", channel instanceof PrivateChannel);

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
                .put("channel", channel)
                .put("user", user)
                .put("response", response);
    }
}
