package pw.sponges.botserver.event.events;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.event.framework.Event;

public class ConnectEvent extends Event {

    private final Client client;

    public ConnectEvent(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public String needsChecks() {
        return null;
    }
}
