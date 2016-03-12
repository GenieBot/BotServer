package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;

public class TestCommand extends Command {

    public TestCommand() {
        super("Simple testing command", "test", "ping", "alive");
    }

    @Override
    public void onCommand(CommandRequest commandRequest, String[] strings) {
        commandRequest.reply("Testing, 1 2 3!");
    }
}
