package io.sponges.botserver.cmd.commands.util;

import io.sponges.botserver.Bot;
import io.sponges.botserver.Client;
import io.sponges.botserver.bridge.Bridge;
import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.bridge.BridgeManager;

public class BridgeCommand extends Command {

    private Bot bot;

    public BridgeCommand(Bot bot) {
        super("command.bridge", UserRole.ADMIN, "bridges the chat of two rooms together", "link", "bridge", "bridgechat");
        this.bot = bot;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
                BridgeManager manager = request.getClient().getBridgeManager();

                if (!manager.isBridged(request.getRoom().getId())) {
                    request.reply("This room is not already bridged?");
                    return;
                }

                Bridge bridge = manager.getBridge(request.getRoom().getId());
                manager.removeBridge(bridge);

                request.reply("Removed the bridge in this room.");
            } else {
                request.reply("Invalid arguments! Usage:\n" +
                        "bridge [client id] [room id]\n" +
                        "bridge remove");
            }

            return;
        } else if (args.length < 2) {
            request.reply("Invalid arguments! Usage:\n" +
                    "bridge [client id] [room id]\n" +
                    "bridge remove");
            return;
        }

        Client client = request.getClient();
        String requestRoom = request.getRoom().getId();
        String targetClient = args[0];
        String targetRoom = args[1];

        request.reply(bot.addLink(client.getId(), requestRoom, targetClient, targetRoom));
    }
}
