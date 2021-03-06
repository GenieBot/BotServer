package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.entities.data.UserData;
import io.sponges.bot.api.entities.message.format.FormattedMessage;
import org.json.JSONObject;

public final class CmdResponseMessage extends Message {

    private final Network network;
    private final Channel channel;
    private final User user;
    private final UserData userData;
    private final String response;
    private final boolean formatted;

    public CmdResponseMessage(Client client, Network network, Channel channel, User user, String response) {
        super(client, "COMMAND_RESPONSE");
        this.network = network;
        this.channel = channel;
        this.user = user;
        this.userData = user.getUserData();
        this.response = response;
        this.formatted = false;
    }

    public CmdResponseMessage(Client client, Network network, Channel channel, User user, FormattedMessage response) {
        super(client, "COMMAND_RESPONSE");
        this.network = network;
        this.channel = channel;
        this.user = user;
        this.userData = user.getUserData();
        this.response = response.getRaw();
        this.formatted = true;
    }

    @Override
    public JSONObject toJson() {
        JSONObject channel = new JSONObject();
        channel.put("id", this.channel.getSourceId());
        channel.put("private", channel instanceof PrivateChannel);

        JSONObject user = new JSONObject();
        user.put("id", this.user.getSourceId());
        if (this.userData.getUsername().isPresent()) {
            user.put("username", this.userData.getUsername().get());
        }
        if (this.userData.getDisplayName().isPresent()) {
            user.put("display-name", this.userData.getDisplayName().get());
        }

        return new JSONObject()
                .put("network", network.getSourceId())
                .put("channel", channel)
                .put("user", user)
                .put("response", response)
                .put("formatted", formatted);
    }
}
