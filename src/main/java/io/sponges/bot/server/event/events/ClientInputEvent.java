package io.sponges.bot.server.event.events;

import io.sponges.bot.server.event.framework.Event;

public class ClientInputEvent extends Event {

    private final String input;

    public ClientInputEvent(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    /*@Override
    public Room needsChecks() {
        return null;
    }*/
}