package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;

public class TestCommand extends Command {

    public TestCommand() {
        super("Ping pong!", "ping", "test");
    }

    @Override
    public void onCommand(CommandRequest commandRequest, String[] strings) {
        commandRequest.reply("Pong");
    }

}
