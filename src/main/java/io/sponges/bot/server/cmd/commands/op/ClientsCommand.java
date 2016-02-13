package io.sponges.bot.server.cmd.commands.op;

import io.sponges.bot.server.Bot;
import io.sponges.bot.server.Client;
import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.messages.StopMessage;

public class ClientsCommand extends Command {

    private Bot bot;

    public ClientsCommand(Bot bot) {
        super("command.op.clients", UserRole.OP, null, "client", "clients");
        this.bot = bot;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply(listClients());
            return;
        }

        String sub = args[0];

        switch (sub.toLowerCase()) {
            case "kick": {
                if (args.length == 1) {
                    request.reply("Usage: clients kick <client>");
                    return;
                }

                String id = args[1];

                if (!bot.getClients().containsKey(id)) {
                    request.reply("Invalid client!");
                    return;
                }

                Client client = bot.getClients().get(id);

                request.reply("Disconnecting " + id + "!");
                client.sendMessage(new StopMessage(client));
                bot.getClients().remove(id);

                break;
            }

            case "stats": {
                request.reply("Stats coming soon!");
                break;
            }

            case "ping": {
                request.reply("Pinging coming soon!");
                break;
            }

            default: {
                request.reply("Invalid arguments!");
            }
        }
    }

    private String listClients() {
        StringBuilder str = new StringBuilder("Connected clients:");

        for (Client client : bot.getClients().values()) {
            str.append("\n- ").append(client.getId());
        }

        str.append("\nAvailable subcommands: kick, stats, ping");
        return str.toString();
    }
}
