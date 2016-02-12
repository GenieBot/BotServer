package io.sponges.botserver.cmd.commands.op;

import io.sponges.botserver.Bot;
import io.sponges.botserver.Client;
import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.messages.StopMessage;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.util.Msg;
import io.sponges.botserver.util.Scheduler;

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
            // Should never happen
            for (Client client : bot.getClients().values()) {
                client.getWrapper().disconnect();
            }

            Msg.warning("BYE! <3");
            try {
                bot.getServer().stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(-1);
        }, 10, TimeUnit.SECONDS);
    }

}
