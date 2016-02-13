package io.sponges.bot.server.messages;

import io.sponges.bot.server.Client;
import io.sponges.bot.server.util.JSONBuilder;
import org.json.JSONObject;

public class JoinRoomMessage extends Message {

    private final String networkId, roomId;

    public JoinRoomMessage(Client client, String networkId, String roomId) {
        super(client, "JOIN");

        this.networkId = networkId;
        this.roomId = roomId;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("network", networkId)
                .withValue("room", roomId)
                .build();
    }

}
