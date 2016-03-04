package io.sponges.bot.server.event.events;

import io.sponges.bot.server.Client;
import io.sponges.bot.server.event.framework.Event;

public class ConnectEvent extends Event {

    private final Client client;

    public ConnectEvent(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
