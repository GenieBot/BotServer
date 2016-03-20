package io.sponges.bot.server.cmd;

import io.sponges.bot.api.cmd.Command;
import io.sponges.bot.api.cmd.CommandRequest;
import io.sponges.bot.server.Bot;

public class ReloadCommand extends Command {

    private final Bot bot;

    public ReloadCommand(Bot bot) {
        super("Reloads all modules", "reload");
        this.bot = bot;
    }

    @Override
    public void onCommand(CommandRequest commandRequest, String[] strings) {
        if (!commandRequest.getUser().isOp()) {
            commandRequest.reply("Sorry, only the bot owner can run this command.");
            return;
        }
        long start = System.currentTimeMillis();
        bot.getModuleManager().reload();
        commandRequest.reply("Reloaded! (" + (System.currentTimeMillis() - start) + "ms)");
    }
}
