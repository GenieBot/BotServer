package io.sponges.bot.server.cmd.commands.info;

import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;

public class UserInfoCommand extends Command {

    public UserInfoCommand() {
        super("command.userinfo", UserRole.USER, "shows info about the user", "userinfo", "aboutme", "me", "myid", "userid");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("ID: " + request.getUser().getId());
    }
}
