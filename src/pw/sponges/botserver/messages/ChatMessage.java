package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class ChatMessage extends Message {

    private Client client;
    private final String user, sourceRoom, sourceName, room, message;

    public ChatMessage(Client client, String user, String sourceRoom, String sourceName, String room, String message) {
        super(client, "CHAT");
        this.client = client;
        this.user = user;
        this.sourceRoom = sourceRoom;
        this.sourceName = sourceName;
        this.room = room;
        this.message = message;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(client)
                .setType(this.getType())
                .withValue("user", user)
                .withValue("source-room", sourceRoom)
                .withValue("name", sourceName)
                .withValue("room", room)
                .withValue("message", message)
                .build();
    }
}