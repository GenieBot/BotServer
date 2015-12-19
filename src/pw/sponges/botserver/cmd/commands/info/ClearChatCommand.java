package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

public class ClearChatCommand extends Command {

    public ClearChatCommand() {
        super("command.about", UserRole.USER, "shows information about the bot", "about", "info");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        // TODO ability to send raw messages so that can print without command response prefix being appended
    }

}
