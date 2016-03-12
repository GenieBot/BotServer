package io.sponges.bot.server.protocol.parser;

import io.sponges.bot.api.entities.Client;
import org.json.JSONObject;

public abstract class MessageParser {

    private final String type;

    protected MessageParser(String type) {
        this.type = type;
    }

    public abstract void parse(Client client, long time, JSONObject content);

    public String getType() {
        return type;
    }
}
