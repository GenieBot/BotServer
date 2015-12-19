package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

public class ChatInfoCommand extends Command {

    public ChatInfoCommand() {
        super("command.chatinfo", UserRole.USER, "shows information about the current room", "chatinfo", "roominfo", "roomid", "chatid");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("ID: " + request.getClient().getId() + "\nRoom: " + request.getRoom() + "");
    }
}
