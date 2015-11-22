package pw.sponges.botserver.cmd.commands.op;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.messages.JoinRoomMessage;

public class JoinRoomCommand extends Command {

    public JoinRoomCommand() {
        super("command.op.joinroom", "joinroom");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: joinroom <room>");
            return;
        }

        String room = args[0];
        request.getClient().sendMessage(new JoinRoomMessage(request.getClient(), room));
        request.reply("Attempting to join that room!");
    }

}
