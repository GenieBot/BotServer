package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;

public class ChatInfoCommand extends Command {

    public ChatInfoCommand() {
        super("command.chatinfo", "chatinfo", "roominfo", "roomid", "chatid");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("ID: " + request.getClient().getId() + "\nRoom: " + request.getRoom() + "");
    }
}
