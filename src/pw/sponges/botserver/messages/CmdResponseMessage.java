package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class CmdResponseMessage extends Message {

    private final Client client;
    private final String room, user, username, response;

    public CmdResponseMessage(Client client, String room, String user, String username, String response) {
        super(client, "COMMAND");

        this.client = client;
        this.room = room;
        this.user = user;
        this.username = username;
        this.response = response;
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this)
                .withValue("room", room)
                .withValue("user", user)
                .withValue("username", username)
                .withValue("response", response)
                .build();
    }

}
