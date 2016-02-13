package io.sponges.bot.server.cmd.commands.op;

import io.sponges.bot.server.Bot;
import io.sponges.bot.server.Client;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.messages.StopMessage;
import io.sponges.bot.server.util.Msg;
import io.sponges.bot.server.util.Scheduler;
import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.storage.UserRole;

import java.util.concurrent.TimeUnit;

public class StopCommand extends Command {

    private Bot bot;

    public StopCommand(Bot bot) {
        super("command.op.stop", UserRole.OP, null, "stop");
        this.bot = bot;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        StopMessage stopMessage = new StopMessage(request.getClient());

        request.reply("bye lol");
        for (Client client : bot.getClients().values()) {
            client.sendMessage(stopMessage);
        }

        Msg.warning("STOPPING IN 10S");

        Scheduler.runAsyncTask(() -> {
            Msg.warning("BYE! <3");
            bot.getServer().stop();
            System.exit(-1);
        }, 10, TimeUnit.SECONDS);
    }

}
