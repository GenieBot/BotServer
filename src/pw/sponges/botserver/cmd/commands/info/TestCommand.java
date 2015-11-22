package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;

public class TestCommand extends Command {

    public TestCommand() {
        super("command.test", "test", "t");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("Testing, 1 2 3!");
    }
}
