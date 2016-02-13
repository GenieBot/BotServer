package io.sponges.bot.server.event.framework;

import io.sponges.bot.server.framework.Room;

public abstract class Event {

    /**
     * Will return the room id if the event will need room loaded checks!
     * @return room id
     */
    public abstract Room needsChecks();

}
