package io.sponges.bot.server.protocol.parser;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.event.internal.ClientInputEvent;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParserManager {

    private final Map<String, MessageParser> parsers = new HashMap<>();

    public ParserManager(Bot bot) {
        register(new ChatMessageParser(bot.getEventManager()));
    }

    private void register(MessageParser parser) {
        parsers.put(parser.getType(), parser);
    }

    public void onClientInput(ClientInputEvent event) {
        Client client = event.getClient();
        JSONObject json = event.getJson();
        String type = json.getString("type").toUpperCase();
        long time = json.getLong("time");
        JSONObject content = json.getJSONObject("content");
        if (parsers.containsKey(type)) {
            parsers.get(type).parse(client, time, content);
        } else {
            System.err.println("Got invalid message type \"" + type + "\"!");
        }
    }

}
