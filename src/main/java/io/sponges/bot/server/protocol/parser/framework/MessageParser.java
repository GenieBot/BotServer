package io.sponges.bot.server.protocol.parser.framework;

import io.sponges.bot.api.entities.Client;
import org.json.JSONObject;

public abstract class MessageParser {

    private final String type;

    public MessageParser(String type) {
        this.type = type;
    }

    public abstract void parse(Client client, long time, String messageId, JSONObject content);

    public String getType() {
        return type;
    }
}
