package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class KickUserMessage extends Message {

    private final String room, user;

    public KickUserMessage(Client client, String room, String user) {
        super(client, "KICK");

        this.room = room;
        this.user = user;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("room", room)
                .withValue("user", user)
                .build();
    }
}
