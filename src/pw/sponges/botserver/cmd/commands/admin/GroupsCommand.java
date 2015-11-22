package pw.sponges.botserver.cmd.commands.admin;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.Setting;

public class GroupsCommand extends Command {

    private Database database;
    private PermissionsManager permissions;

    public GroupsCommand(Database database, PermissionsManager permissions) {
        super("command.groups", "groups");
        this.database = database;
        this.permissions = permissions;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Invalid arguments! Usage: prefix <new prefix>");
            return;
        }

        String prefix = args[0];
        database.getData(request.getRoom()).getSettings().set(Setting.PREFIX, prefix);
        database.save(request.getRoom());

        request.reply("Set the prefix for " + request.getRoom() + " to " + args[0] + "!");
    }
}
