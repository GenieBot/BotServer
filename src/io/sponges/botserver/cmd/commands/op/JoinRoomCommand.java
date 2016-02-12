package io.sponges.botserver.cmd.commands.op;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.messages.JoinRoomMessage;
import io.sponges.botserver.storage.UserRole;

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
