package io.sponges.bot.server.messages;

import io.sponges.bot.server.Client;
import io.sponges.bot.server.util.JSONBuilder;
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
