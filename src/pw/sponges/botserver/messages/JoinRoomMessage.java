package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

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
