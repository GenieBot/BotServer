package io.sponges.botserver.messages;

import io.sponges.botserver.Client;
import io.sponges.botserver.util.JSONBuilder;
import org.json.JSONObject;

public class StopMessage extends Message {

    public StopMessage(Client client) {
        super(client, "STOP");
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this).build();
    }

}
