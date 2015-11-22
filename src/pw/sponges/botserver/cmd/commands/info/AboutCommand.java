package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;

public class AboutCommand extends Command {

    public AboutCommand() {
        super("command.about", "about", "info");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("Stuff about SpongyBot!");
    }

}
