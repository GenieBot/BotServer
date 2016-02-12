package io.sponges.botserver.cmd.commands.info;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.util.StringUtils;
import io.sponges.botserver.BotListener;

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
