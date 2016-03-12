package io.sponges.bot.server.event.internal;

import io.sponges.bot.api.entities.Client;
import org.json.JSONObject;

public final class ClientInputEvent extends InternalEvent {

    private final Client client;
    private final JSONObject json;

    public ClientInputEvent(Client client, JSONObject json) {
        this.client = client;
        this.json = json;
    }

    public Client getClient() {
        return client;
    }

    public JSONObject getJson() {
        return json;
    }
}
