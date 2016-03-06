package io.sponges.bot.server.event.internal;

import io.sponges.bot.api.entities.Client;

public final class ClientInputEvent extends InternalEvent {

    private final Client client;
    private final String input;

    public ClientInputEvent(Client client, String input) {
        this.client = client;
        this.input = input;
    }

    public Client getClient() {
        return client;
    }

    public String getInput() {
        return input;
    }
}
