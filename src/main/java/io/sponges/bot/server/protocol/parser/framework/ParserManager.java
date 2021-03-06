package io.sponges.bot.server.protocol.parser.framework;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.event.events.msg.ProtocolMessageReceiveEvent;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.event.internal.ClientInputEvent;
import io.sponges.bot.server.protocol.parser.parsers.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParserManager {

    private final Map<String, MessageParser> parsers = new HashMap<>();

    private final Bot bot;

    public ParserManager(Bot bot) {
        this.bot = bot;

        register(
                new ChatMessageParser(bot.getEventBus()),
                new UserJoinMessageParser(bot.getEventBus()),
                new ResourceResponseParser(bot),
                new ChannelDataUpdateParser(bot.getEventBus())
        );
    }

    private void register(MessageParser... parsers) {
        for (MessageParser parser : parsers) {
            register(parser);
        }
    }

    private void register(MessageParser parser) {
        parsers.put(parser.getType(), parser);
    }

    public void onClientInput(ClientInputEvent event) {
        Client client = event.getClient();
        JSONObject json = event.getJson();
        Bot.LOGGER.log(Logger.Type.DEBUG, json.toString());
        String type = json.getString("type").toUpperCase();
        long time = json.getLong("time");
        JSONObject content = json.getJSONObject("content");
        if (parsers.containsKey(type)) {
            parsers.get(type).parse(client, time, content);
        } else {
            if (!type.equals("CONNECT")) Bot.LOGGER.log(Logger.Type.WARNING, "Got invalid message type \"" + type + "\"!");
        }
        bot.getEventBus().post(new ProtocolMessageReceiveEvent());
    }

}
