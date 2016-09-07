package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.Logger;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.event.framework.EventManager;
import io.sponges.bot.server.Bot;
import io.sponges.bot.server.event.impl.ChannelMessageReceiveEventImpl;
import io.sponges.bot.server.protocol.manager.ChannelMessageManager;
import io.sponges.bot.server.protocol.msg.ChannelMessage;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import org.json.JSONObject;

import java.util.function.Consumer;

public final class ChannelMessageParser extends MessageParser {

    private final EventManager eventManager;

    public ChannelMessageParser(EventManager eventManager) {
        super("CHANNEL_MESSAGE");
        this.eventManager = eventManager;
    }

    @Override
    public void parse(Client client, long time, JSONObject content) {
        String message = content.getString("message");
        String id = content.getString("id");
        ChannelMessage.MessageType type = ChannelMessage.MessageType.valueOf(content.getString("type").toUpperCase());
        if (type == ChannelMessage.MessageType.RESPONSE) {
            ChannelMessageManager manager = ChannelMessage.MESSAGE_MANAGER;
            if (!manager.getMessages().containsKey(id)) {
                return;
            }
            Consumer<String> callback = manager.getMessages().get(id);
            Bot.LOGGER.log(Logger.Type.DEBUG, "got channel message id=" + id + "message=" + message + "consumer=" + callback.toString());
            callback.accept(message);
        } else {
            eventManager.post(new ChannelMessageReceiveEventImpl(client, message, id));
        }
    }
}
