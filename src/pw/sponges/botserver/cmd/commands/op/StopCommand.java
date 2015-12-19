package pw.sponges.botserver.cmd.commands.op;

import pw.sponges.botserver.Bot;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.messages.StopMessage;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.util.Msg;
import pw.sponges.botserver.util.Scheduler;

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
