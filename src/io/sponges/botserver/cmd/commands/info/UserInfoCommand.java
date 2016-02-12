package io.sponges.botserver.cmd.commands.info;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;

public class UserInfoCommand extends Command {

    public UserInfoCommand() {
        super("command.userinfo", UserRole.USER, "shows info about the user", "userinfo", "aboutme", "me", "myid", "userid");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("ID: " + request.getUser().getId());
    }
}
