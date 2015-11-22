package pw.sponges.botserver.event.events;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.event.framework.Event;

public class LinkRequestEvent extends Event {

    private final Client client;
    private final String clientRoom, targetId, targetRoom;

    public LinkRequestEvent(Client client, String clientRoom, String targetId, String targetRoom) {
        this.client = client;
        this.clientRoom = clientRoom;
        this.targetId = targetId;
        this.targetRoom = targetRoom;
    }

    public Client getClient() {
        return client;
    }

    public String getClientRoom() {
        return clientRoom;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getTargetRoom() {
        return targetRoom;
    }

    @Override
    public String needsChecks() {
        return clientRoom;
    }
}