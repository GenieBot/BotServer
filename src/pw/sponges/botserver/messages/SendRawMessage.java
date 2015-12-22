package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class SendRawMessage extends Message {

    private final String room, message;

    public SendRawMessage(Client client, String room, String message) {
        super(client, "RAW");

        this.room = room;
        this.message = message;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("room", room)
                .withValue("message", message)
                .build();
    }

}
