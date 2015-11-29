package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;

public abstract class Message {

    private final Client client;
    private final String type;

    public Message(Client client, String type) {
        this.client = client;
        this.type = type;
    }

    public abstract JSONObject toJson();

    public Client getClient() {
        return client;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

}
