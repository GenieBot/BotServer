package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.event.framework.Event;
import io.sponges.bot.api.event.framework.EventManager;

import java.util.function.Consumer;

public class TestCommand extends Command {

    private final EventManager eventManager;

    public TestCommand(EventManager eventManager) {
        super("Simple testing command", "test", "ping", "alive");
        this.eventManager = eventManager;
    }

    @Override
    public void onCommand(CommandRequest commandRequest, String[] strings) {
        Consumer<TestEvent> consumer = testEvent -> System.out.println("Got Ze Event BOiz " + testEvent.toString());
        eventManager.register(null, TestEvent.class, consumer);
        Event event = new TestEvent();
        event.setTimeSlot(100); // 100ms cancellation window
        eventManager.postAsync(event);
        event.setCancelled(true);
        commandRequest.reply("okey dokey");
    }

    public class TestEvent extends Event {
        public TestEvent() {
            super(true); // the event is cancellable
        }
    }

}
