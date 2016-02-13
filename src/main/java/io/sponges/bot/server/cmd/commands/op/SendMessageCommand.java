package io.sponges.bot.server.cmd.commands.op;

import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.messages.SendRawMessage;
import io.sponges.bot.server.storage.UserRole;

public class SendMessageCommand extends Command {

    public SendMessageCommand() {
        super("command.op.sendmessage", UserRole.OP, null, "sendmessage", "pm");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length < 2) {
            request.reply("stupid human... usage: sendmessage [network] [room] [message]");
            return;
        }

        String network = args[0];
        String room = args[1];

        StringBuilder message = new StringBuilder();

        for (int i = 2; i < args.length; i++) {
            message.append(args[i]);

            if (i != args.length - 1) {
                message.append(" ");
            }
        }

        request.getClient().sendMessage(new SendRawMessage(request.getClient(), network, room, message.toString()));
    }
}
