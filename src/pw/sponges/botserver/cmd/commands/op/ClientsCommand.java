package pw.sponges.botserver.cmd.commands.op;

import pw.sponges.botserver.Bot;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.messages.StopMessage;
import pw.sponges.botserver.permissions.simple.UserRole;

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

                if (!bot.isClient(id)) {
                    request.reply("Invalid client!");
                    return;
                }

                Client client = bot.getClient(id);

                request.reply("Disconnecting " + id + "!");
                client.sendMessage(new StopMessage(client));
                client.getWrapper().disconnect();

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
