package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class ChatMessage extends Message {

    private Client client;
    private final String userId, username, sourceRoom, sourceName, room, message;

    public ChatMessage(Client client, String userId, String username, String sourceRoom, String sourceName, String room, String message) {
        super(client, "CHAT");
        this.client = client;
        this.userId = userId;
        this.username = username;
        this.sourceRoom = sourceRoom;
        this.sourceName = sourceName;
        this.room = room;
        this.message = message;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("userid", userId)
                .withValue("username", username)
                .withValue("source-room", sourceRoom)
                .withValue("name", sourceName)
                .withValue("room", room)
                .withValue("message", message)
                .build();
    }
}