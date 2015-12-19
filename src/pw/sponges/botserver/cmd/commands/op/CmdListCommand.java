package pw.sponges.botserver.cmd.commands.op;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandHandler;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

public class CmdListCommand extends Command {

    private final CommandHandler handler;

    public CmdListCommand(CommandHandler handler) {
        super("command.op.cmdlist", UserRole.OP, null, "cmdlist", "commandlist", "telegramcommandlist", "tgcommandlist", "tgcmd");

        this.handler = handler;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        StringBuilder builder = new StringBuilder();

        for (Command cmd : handler.getCommands()) {
            if (cmd.getDescription() == null) {
                continue;
            }

            builder.append(cmd.getNames()[0]).append(" - ").append(cmd.getDescription()).append("\n");
        }

        request.reply(builder.toString());
    }
}
