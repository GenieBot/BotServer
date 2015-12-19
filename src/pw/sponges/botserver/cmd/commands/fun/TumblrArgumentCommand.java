package pw.sponges.botserver.cmd.commands.fun;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.util.TumblrArgument;

public class TumblrArgumentCommand extends Command {

    private final TumblrArgument tumblrArgument;

    public TumblrArgumentCommand() {
        super("command.tumblrargument", UserRole.USER, "generates a random tumblr argument", "tumblr", "tumblrargument");
        this.tumblrArgument = new TumblrArgument();
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply(tumblrArgument.generateArgument());
    }

}
