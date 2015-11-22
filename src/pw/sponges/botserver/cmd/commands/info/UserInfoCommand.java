package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;

public class UserInfoCommand extends Command {

    public UserInfoCommand() {
        super("command.userinfo", "userinfo", "aboutme", "me", "myid", "userid");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("ID: " + request.getUser());
    }
}
