package io.sponges.bot.server.event.events;

import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.event.framework.Event;

public class CommandRequestEvent extends Event {

    private final CommandRequest commandRequest;

    public CommandRequestEvent(CommandRequest commandRequest) {
        this.commandRequest = commandRequest;
    }

    public CommandRequest getCommandRequest() {
        return commandRequest;
    }

    /*@Override
    public Room needsChecks() {
        return null;
    }*/
}
