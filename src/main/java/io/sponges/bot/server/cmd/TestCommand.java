package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.api.event.framework.EventManager;

public class TestCommand extends Command {

    private final EventManager eventManager;

    public TestCommand(EventManager eventManager) {
        super("Simple testing command", "test", "ping", "alive");
        this.eventManager = eventManager;
    }

    @Override
    public void onCommand(CommandRequest commandRequest, String[] strings) {
        commandRequest.reply("hi");
    }

}
