package io.sponges.bot.server.cmd.commands.op;

import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.messages.JoinRoomMessage;
import io.sponges.bot.server.storage.UserRole;

public class JoinRoomCommand extends Command {

    public JoinRoomCommand() {
        super("command.op.joinroom", UserRole.OP, null, "joinroom");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: joinroom <network> <room>");
            return;
        }

        String network = args[0];
        String room = args[1];
        request.getClient().sendMessage(new JoinRoomMessage(request.getClient(), network, room));
        request.reply("Attempting to join that room!");
    }

}
