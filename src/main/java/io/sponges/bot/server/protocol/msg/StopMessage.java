package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import org.json.JSONObject;

public final class StopMessage extends Message {

    public StopMessage(Client client) {
        super(client, "STOP");
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }
}
