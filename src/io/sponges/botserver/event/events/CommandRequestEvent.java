package io.sponges.botserver.event.events;

import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.event.framework.Event;
import io.sponges.botserver.framework.Room;

public class CommandRequestEvent extends Event {

    private final CommandRequest commandRequest;

    public CommandRequestEvent(CommandRequest commandRequest) {
        this.commandRequest = commandRequest;
    }

    public CommandRequest getCommandRequest() {
        return commandRequest;
    }

    @Override
    public Room needsChecks() {
        return null;
    }
}
