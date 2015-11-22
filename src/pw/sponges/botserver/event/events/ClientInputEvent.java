package pw.sponges.botserver.event.events;

import pw.sponges.botserver.event.framework.Event;
import pw.sponges.botserver.internal.ServerThread;

public class ClientInputEvent extends Event {

    private final String input;
    private final ServerThread thread;

    public ClientInputEvent(String input, ServerThread thread) {
        this.input = input;
        this.thread = thread;
    }

    public String getInput() {
        return input;
    }

    public ServerThread getThread() {
        return thread;
    }

    @Override
    public String needsChecks() {
        return null;
    }
}