package io.sponges.bot.server.cmd.commands.admin;

import io.sponges.bot.server.Client;
import io.sponges.bot.server.messages.SendRawMessage;
import org.apache.commons.lang3.StringEscapeUtils;
import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;

public class SpamCommand extends Command {

    public SpamCommand() {
        super("command.spam", UserRole.OP, "spams stuff lol", "spam");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("... usage: spam [# times] [message]");
            return;
        }

        int amount = 0;

        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {
            request.reply("Invalid integer " + args[0] + "!");
            return;
        }

        if (amount == 0) return;

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(StringEscapeUtils.unescapeJson(args[i]));

            if (i != args.length - 1) {
                message.append(" ");
            }
        }

        Client client = request.getClient();
        String m = message.toString();
        SendRawMessage raw = new SendRawMessage(client, request.getNetwork().getId(), request.getRoom().getId(), m);

        for (int i = 0; i < amount; i++) {
            client.sendMessage(raw);
        }
    }

}
