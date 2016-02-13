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

    // TODO improve stats

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        StringBuilder str = new StringBuilder("Statistics:");
        str.append("\n").append("Server messages: ").append(BotListener.getServerMessages());
        str.append("\n").append("Chat messages: ").append(BotListener.getChatMessages());
        str.append("\n").append("Command runs: ").append(BotListener.getCommandRuns());
        str.append("\n").append("Uptime: ").append(StringUtils.formatDate(startTime, System.currentTimeMillis()));

        request.reply(str.toString());
    }

}
