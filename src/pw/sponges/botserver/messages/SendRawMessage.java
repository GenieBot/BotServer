package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class SendRawMessage extends Message {

    private final String network, room, message;

    public SendRawMessage(Client client, String network, String room, String message) {
        super(client, "RAW");

        this.network = network;
        this.room = room;
        this.message = message;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("network", network)
                .withValue("room", room)
                .withValue("message", message)
                .build();
    }
}
