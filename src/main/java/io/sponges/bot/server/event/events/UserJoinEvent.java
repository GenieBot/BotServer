package io.sponges.bot.server.event.events;

import io.sponges.bot.server.event.framework.Event;
import io.sponges.bot.server.framework.User;

public class UserJoinEvent extends Event {

    /*private final Client client;
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
    }*/

    private final User user;

    public UserJoinEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
