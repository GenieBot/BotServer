package pw.sponges.botserver.cmd.commands.util;

import pw.sponges.botserver.Bot;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;

public class LinkCommand extends Command {

    private Bot bot;

    public LinkCommand(Bot bot) {
        super("command.link", "link", "bridge", "bridgechat");
        this.bot = bot;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length <= 1) {
            request.reply("Invalid arguments! Usage: bridgechat <client id> <room id>");
            return;
        }

        Client client = request.getClient();
        String requestRoom = request.getRoom();
        String targetClient = args[0];
        String targetRoom = args[1];

        request.reply(bot.addLink(client.getId(), requestRoom, targetClient, targetRoom));
    }
}
