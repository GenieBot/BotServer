package io.sponges.bot.server.event.events;

import io.sponges.bot.server.event.framework.Event;
import io.sponges.bot.server.framework.Room;

public class ClientInputEvent extends Event {

    private final String input;

    public ClientInputEvent(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    @Override
    public Room needsChecks() {
        return null;
    }
}