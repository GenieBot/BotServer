package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class PrefixChangeMessage extends Message {

    private final String room, prefix;

    public PrefixChangeMessage(Client client, String room, String prefix) {
        super(client, "PREFIX");
        this.room = room;
        this.prefix = prefix;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(getClient())
                .setType(getType())
                .withValue("room", room)
                .withValue("prefix", prefix)
                .build();
    }

}
