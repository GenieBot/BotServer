package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.server.protocol.manager.ChannelMessageManager;
import org.json.JSONObject;

import java.util.UUID;
import java.util.function.Consumer;

public final class ChannelMessage extends Message {

    public static final ChannelMessageManager MESSAGE_MANAGER = new ChannelMessageManager();

    private final String message;
    private final String id;

    public ChannelMessage(Client client, String message, Consumer<String> callback) {
        super(client, "CHANNEL_MESSAGE");
        this.message = message;
        this.id = UUID.randomUUID().toString();
        if (callback != null) MESSAGE_MANAGER.getMessages().put(id, callback);
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject()
                .put("message", message)
                .put("id", id);
    }

}
