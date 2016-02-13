package io.sponges.bot.server.messages;

import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.framework.User;
import io.sponges.bot.server.util.JSONBuilder;
import org.json.JSONObject;

public class ChatMessage extends Message {

    private final User user;
    private final Room localRoom;
    private final Room target;
    private final String message;

    public ChatMessage(User user, Room target, String message) {
        super(user.getClient(), "CHAT");

        this.user = user;
        this.localRoom = user.getRoom();
        this.target = target;
        this.message = message;
    }

    // TODO convert message to json objects, network support
    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("userid", user.getId())
                .withValue("usernaeme", user.getUsername())
                .withValue("source-room", localRoom.getId())
                .withValue("name", localRoom.getTopic())
                .withValue("room", target.getId())
                .withValue("message", message)
                .build();
    }
}