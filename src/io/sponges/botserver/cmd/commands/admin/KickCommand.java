package io.sponges.botserver.cmd.commands.admin;

import io.sponges.botserver.Client;
import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.messages.KickUserMessage;

public class KickCommand extends Command {

    public KickCommand() {
        super("command.kick", UserRole.ADMIN, "kick a user from the chat", "kick", "kickuser", "kick", "removeuser", "kickmember", "deleteuser");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Do you have an IQ? Usage: kick [user id/username]\nYou can also kick multiple users at once with 'kick [username,username,username]'");
            return;
        }

        StringBuilder str = new StringBuilder();
        for (String arg : args) {
            str.append(arg);
        }
        String full = str.toString();

        Client client = request.getClient();

        if (!full.contains(",")) {
            client.sendMessage(new KickUserMessage(client, request.getRoom(), args[0]));
        } else {
            for (String username : full.split(",")) {
                client.sendMessage(new KickUserMessage(client, request.getRoom(), username));
            }
        }
    }

}