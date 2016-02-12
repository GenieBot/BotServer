package io.sponges.botserver.event.events;

import io.sponges.botserver.Client;
import io.sponges.botserver.event.framework.Event;
import io.sponges.botserver.framework.Room;

public class ConnectEvent extends Event {

    private final Client client;

    public ConnectEvent(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public Room needsChecks() {
        return null;
    }
}
