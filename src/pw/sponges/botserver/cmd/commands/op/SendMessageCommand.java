package pw.sponges.botserver.cmd.commands.op;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

public class SendMessageCommand extends Command {

    public SendMessageCommand() {
        super("command.op.sendmessage", UserRole.OP, null, "sendmessage", "pm");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length < 1) {
            request.reply("stupid human... usage: sendmessage [room] [message]");
            return;
        }

        String room = args[0];

        StringBuilder message = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            message.append(args[i]);

            if (i != args.length - 1) {
                message.append(" ");
            }
        }

        // TODO fix sending raw messages to non loaded rooms
        //request.getClient().sendMessage(new SendRawMessage(room, message.toString()));
    }
}
