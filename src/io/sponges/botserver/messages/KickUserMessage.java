package io.sponges.botserver.messages;

import io.sponges.botserver.Client;
import io.sponges.botserver.framework.Room;
import org.json.JSONObject;
import io.sponges.botserver.framework.User;
import io.sponges.botserver.util.JSONBuilder;

public class KickUserMessage extends Message {

    private final Room room;
    private final String user;

    public KickUserMessage(User user) {
        super(user.getClient(), "KICK");

        this.room = user.getRoom();
        this.user = user.getId();
    }

    public KickUserMessage(Client client, Room room, String user) {
        super(client, "KICK");

        this.room = room;
        this.user = user;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("network", room.getNetwork().getId())
                .withValue("room", room.getId())
                .withValue("user", user)
                .build();
    }
}
