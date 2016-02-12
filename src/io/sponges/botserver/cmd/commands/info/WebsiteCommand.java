package io.sponges.botserver.cmd.commands.info;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;

public class WebsiteCommand extends Command {

    public WebsiteCommand() {
        super("command.website", UserRole.USER, "links to the spongybot subreddit", "website", "subreddit", "site", "web", "sr", "officialsite", "suggest", "suggestions", "idea");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        request.reply("The official SpongyBot subreddit: https://www.reddit.com/r/spongybot/");
    }

}
