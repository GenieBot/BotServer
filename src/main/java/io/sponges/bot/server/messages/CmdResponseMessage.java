package io.sponges.bot.server.messages;

import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.framework.User;
import io.sponges.bot.server.util.JSONBuilder;
import io.sponges.bot.server.framework.Network;
import org.json.JSONObject;

public class CmdResponseMessage extends Message {

    private final User user;
    private final Room room;
    private final Network network;
    private final String response;

    public CmdResponseMessage(User user, String response) {
        super(user.getClient(), "COMMAND");

        this.user = user;
        this.room = user.getRoom();
        this.network = room.getNetwork();
        this.response = response;
    }

    // TODO change this to json objects with more data
    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("network", network.getId())
                .withValue("room", room.getId())
                .withValue("user", user.getId())
                .withValue("username", user.getUsername())
                .withValue("response", response)
                .build();
    }
}