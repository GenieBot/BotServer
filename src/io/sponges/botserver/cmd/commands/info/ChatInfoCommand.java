package io.sponges.botserver.cmd.commands.info;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;

public class ChatInfoCommand extends Command {

    public ChatInfoCommand() {
        super("command.chatinfo", UserRole.USER, "shows information about the current room", "chatinfo", "roominfo", "roomid", "chatid");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("ID: " + request.getClient().getId()
                + "\nNetwork: " + request.getNetwork().getId()
                + "\nRoom: " + request.getRoom().getId());
    }
}
