package io.sponges.bot.server.cmd.commands.info;

import io.sponges.bot.server.BotListener;
import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.util.StringUtils;

public class StatsCommand extends Command {

    private final long startTime;

    public StatsCommand() {
        super("command.stats", UserRole.USER, "shows bot statistics", "stats", "statistics", "uptime");
        this.startTime = System.currentTimeMillis();
    }

    // TODO improve stats, stats caching

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("Statistics:"
                + "\nServer messages: " + BotListener.getServerMessages()
                + "\nChat messages: " + BotListener.getChatMessages()
                + "\nCommand runs: " + BotListener.getCommandRuns()
                + "\nUptime: " + StringUtils.formatDate(startTime, System.currentTimeMillis()));
    }

}
