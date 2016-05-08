package io.sponges.bot.server.protocol.msg;

import io.sponges.bot.api.entities.Client;
import io.sponges.bot.server.protocol.manager.ChannelMessageManager;
import org.json.JSONObject;

import java.util.UUID;
import java.util.function.Consumer;

public final class ChannelMessage extends Message {

    public static final ChannelMessageManager MESSAGE_MANAGER = new ChannelMessageManager();

    private final MessageType type;
    private final String message;
    private final String id;

    public ChannelMessage(Client client, String id, String message, Consumer<String> callback, MessageType type) {
        super(client, "CHANNEL_MESSAGE");
        this.message = message;
        this.type = type;
        if (type == MessageType.REQUEST) this.id = UUID.randomUUID().toString();
        else this.id = id;
        if (type == MessageType.REQUEST && callback != null) MESSAGE_MANAGER.getMessages().put(id, callback);
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject()
                .put("message", message)
                .put("type", type.toString())
                .put("id", id);
    }

    public enum MessageType {
        REQUEST, RESPONSE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

}
