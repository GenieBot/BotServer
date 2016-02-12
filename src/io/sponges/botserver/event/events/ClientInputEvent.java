package io.sponges.botserver.event.events;

import io.sponges.botserver.event.framework.Event;
import io.sponges.botserver.framework.Room;

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