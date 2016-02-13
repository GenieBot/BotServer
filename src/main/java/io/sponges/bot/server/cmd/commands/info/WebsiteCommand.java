package io.sponges.bot.server.cmd.commands.info;

import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;

public class WebsiteCommand extends Command {

    public WebsiteCommand() {
        super("command.website", UserRole.USER, "links to the spongybot subreddit", "website", "subreddit", "site", "web", "sr", "officialsite", "suggest", "suggestions", "idea");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("The official SpongyBot subreddit: https://www.reddit.com/r/spongybot/");
    }

}
