package io.sponges.bot.server.protocol.parser.parsers;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.server.protocol.manager.ChannelMessageManager;
import io.sponges.bot.server.protocol.msg.ChannelMessage;
import io.sponges.bot.server.protocol.parser.framework.MessageParser;
import org.json.JSONObject;

public final class ChannelMessageParser extends MessageParser {

    public ChannelMessageParser() {
        super("CHANNEL_MESSAGE");
    }

    @Override
    public void parse(Client client, long time, JSONObject content) {
        String message = content.getString("message");
        String id = content.getString("id");
        ChannelMessageManager manager = ChannelMessage.MESSAGE_MANAGER;
        if (!manager.getMessages().containsKey(id)) {
            return;
        }
        manager.getMessages().get(id).accept(message);
    }
}
