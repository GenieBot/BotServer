package io.sponges.bot.server.cmd.commands.info;

import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;

public class TestCommand extends Command {

    public TestCommand() {
        super("command.test", UserRole.USER, "like ping pong but without a ball", "test", "t");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("Testing, 1 2 3");
    }
}
