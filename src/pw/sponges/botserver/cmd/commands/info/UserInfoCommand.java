package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

public class UserInfoCommand extends Command {

    public UserInfoCommand() {
        super("command.userinfo", UserRole.USER, "shows info about the user", "userinfo", "aboutme", "me", "myid", "userid");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("ID: " + request.getUser());
    }
}
