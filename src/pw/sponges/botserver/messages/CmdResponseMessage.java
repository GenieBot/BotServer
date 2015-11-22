package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class CmdResponseMessage extends Message {

    private final Client client;
    private final String room, user, response;
    private final ResponseOption option;

    public CmdResponseMessage(Client client, String room, String user, String response, ResponseOption option) {
        super(client, "COMMAND");

        this.client = client;
        this.room = room;
        this.user = user;
        this.response = response;
        this.option = option;
    }

    @Override
    public JSONObject getJSON() {
        return JSONBuilder.create(client)
                .setType(getType())
                .withValue("room", room)
                .withValue("user", user)
                .withValue("option", option.toString())
                .withValue("response", response)
                .build();
    }

}
