package pw.sponges.botserver.event.events;

import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.event.framework.Event;

public class CommandRequestEvent extends Event {

    private final CommandRequest commandRequest;

    public CommandRequestEvent(CommandRequest commandRequest) {
        this.commandRequest = commandRequest;
    }

    public CommandRequest getCommandRequest() {
        return commandRequest;
    }

    @Override
    public String needsChecks() {
        return null;
    }
}
