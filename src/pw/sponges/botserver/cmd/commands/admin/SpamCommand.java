package pw.sponges.botserver.cmd.commands.admin;

import org.apache.commons.lang3.StringEscapeUtils;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

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
        // TODO sending raw messages to non loaded rooms
        //SendRawMessage raw = new SendRawMessage(client, request.getRoom(), m);

        for (int i = 0; i < amount; i++) {
            //client.sendMessage(raw);
        }
    }

}
