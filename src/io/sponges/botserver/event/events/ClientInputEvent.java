package io.sponges.botserver.event.events;

import io.sponges.botserver.framework.Room;
import io.sponges.botserver.event.framework.Event;
import io.sponges.botserver.internal.ServerWrapper;

public class ClientInputEvent extends Event {

    private final String input;
    private final ServerWrapper wrapper;

    public ClientInputEvent(String input, ServerWrapper wrapper) {
        this.input = input;
        this.wrapper = wrapper;
    }

    public String getInput() {
        return input;
    }

    public ServerWrapper getWrapper() {
        return wrapper;
    }

    @Override
    public Room needsChecks() {
        return null;
    }
}