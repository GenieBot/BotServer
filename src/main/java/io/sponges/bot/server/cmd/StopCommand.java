package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.server.Bot;

public class StopCommand extends Command {

    private final Bot bot;

    public StopCommand(Bot bot) {
        super("Stops the server", "stop");
        this.bot = bot;
    }

    @Override
    public void onCommand(CommandRequest commandRequest, String[] strings) {
        if (!commandRequest.getUser().isOp()) {
            commandRequest.reply("Sorry, only the bot owner can run this command.");
            return;
        }
        commandRequest.reply("Bye!");
        bot.stop();
    }
}
