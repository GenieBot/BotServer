package pw.sponges.botserver.cmd.commands.info;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;

public class AboutCommand extends Command {

    private String message = null;

    public AboutCommand() {
        super("command.about", UserRole.USER, "shows information about the bot", "about", "info");

        if (message == null) reloadMessage();
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply(message);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reloadMessage();
        }
    }

    private void reloadMessage() {
        StringBuilder str = new StringBuilder();

        str.append("About SpongyBot:\n");
        str.append("A really fucking cool instant messaging bot supporting Skype, Discord, Telegram, Slack and IRC\n");
        str.append("Subreddit: https://www.reddit.com/r/spongybot/\n");
        str.append("Thanks to... https://redd.it/3xlfdh\n");
        str.append("Official chats: https://redd.it/3xle28\n");
        str.append("Sponsored by NodeBrewery (coming soon)");

        message = str.toString();
    }

}
