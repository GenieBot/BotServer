package io.sponges.bot.server.cmd.commands.fun;

import io.sponges.bot.server.util.TumblrArgument;
import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;

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
