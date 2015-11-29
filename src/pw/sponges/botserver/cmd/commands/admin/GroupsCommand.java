package pw.sponges.botserver.cmd.commands.admin;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.Group;
import pw.sponges.botserver.permissions.PermissionGroups;
import pw.sponges.botserver.permissions.PermissionsManager;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.util.Msg;

import java.util.Arrays;
import java.util.Map;

public class GroupsCommand extends Command {

    private Database database;
    private PermissionsManager permissions;

    public GroupsCommand(Database database, PermissionsManager permissions) {
        super("command.groups", "groups", "permissions", "perms", "myperms", "pex", "group", "groupmanager");
        this.database = database;
        this.permissions = permissions;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Groups subcommands:\nme, list, user");
            return;
        }

        PermissionGroups groups = permissions.getGroups(request.getRoom());

        switch (args[0].toLowerCase()) {
            case "me": {
                request.reply(getInfo(groups, request.getUser()));
                break;
            }

            case "list": {
                StringBuilder str = new StringBuilder("Groups for this room:\n");
                Map<String, Group> groupsMap = groups.getGroups();

                for (String s : groupsMap.keySet()) {
                    str.append(s).append(", ");
                }

                request.reply(str.toString());
                break;
            }

            case "user": {
                if (args.length == 1) {
                    request.reply("Usage: user [username] [group]");
                } else if (args.length == 2) {
                    // user info
                    String user = args[1];
                    request.reply(getInfo(groups, user));
                } else if (args.length == 3) {
                    String user = args[1];
                    String group = args[2];
                    Msg.debug("Group command: " + Arrays.toString(args));
                    Msg.debug("User: " + user);
                    Msg.debug("Group: " + group);

                    if (!groups.isGroup(group)) {
                        request.reply("Invalid group " + group + "!");
                        return;
                    }
                    Group g = groups.getGroup(group);
                    Msg.debug("Chosen group: " + g.getId());

                    groups.setGroup(user, g);
                    database.save(request.getRoom());
                    request.reply("Set " + user + "'s group to " + group + "!");
                } else {
                    request.reply("Invalid arguments!");
                }

                break;
            }

            default: {
                request.reply("Invalid arguments!");
                break;
            }
        }
    }

    private String getInfo(PermissionGroups groups, String user) {
        StringBuilder str = new StringBuilder(user + "'s group stuff:");

        Group group = groups.getUserGroup(user);
        str.append("\nGroup: ").append(group.getId());

        str.append("\nPermissions:");
        for (String node : group.getPermissionNodes()) {
            str.append("\n- ").append(node);
        }

        Group inheritance = group.getInheritance();
        if (inheritance != null) {
            str.append("\nInherit: ").append(inheritance.getId());
            for (String node : inheritance.getPermissionNodes()) {
                str.append("\n- ").append(node);
            }
        }

        return str.toString();
    }

}
