package pw.sponges.botserver.event.events;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.event.framework.Event;

public class UserJoinEvent extends Event {

    private final Client client;
    private final String room, user;

    public UserJoinEvent(Client client, String room, String user) {
        this.client = client;
        this.room = room;
        this.user = user;
    }

    public Client getClient() {
        return client;
    }

    public String getRoom() {
        return room;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String needsChecks() {
        return room;
    }

}
