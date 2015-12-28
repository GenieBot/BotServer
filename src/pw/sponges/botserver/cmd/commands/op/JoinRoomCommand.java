package pw.sponges.botserver.cmd.commands.op;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.messages.JoinRoomMessage;
import pw.sponges.botserver.permissions.simple.UserRole;

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
