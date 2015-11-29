package pw.sponges.botserver.event.events;

import pw.sponges.botserver.event.framework.Event;
import pw.sponges.botserver.internal.ServerWrapper;

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
    public String needsChecks() {
        return null;
    }
}