package io.sponges.botserver.cmd.commands.fun;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.util.TumblrArgument;

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
