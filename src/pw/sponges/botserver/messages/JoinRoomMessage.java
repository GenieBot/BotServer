package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class JoinRoomMessage extends Message {

    private final String room;

    public JoinRoomMessage(Client client, String room) {
        super(client, "JOIN");
        this.room = room;
    }

    @Override
    public JSONObject getJSON() {
        return JSONBuilder.create(getClient())
                .setType(getType())
                .withValue("room", room)
                .build();
    }

}
