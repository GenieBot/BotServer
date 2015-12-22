package pw.sponges.botserver.cmd.commands.admin;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandHandler;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.RoomSettings;
import pw.sponges.botserver.storage.Setting;

import java.util.Arrays;
import java.util.List;

public class ToggleCommand extends Command {

    private final Database database;
    private final CommandHandler handler;

    private final List<String> noDisable = Arrays.asList("groups", "prefix", "settings", "togglecommand", "clients",
            "cmdlist", "joinroom", "sendmessage", "stop");

    public ToggleCommand(Database database, CommandHandler handler) {
        super("command.togglecommand", UserRole.ADMIN, "enables and disables a command", "togglecommand", "toggle", "enable", "disable", "ec", "dc", "tc");

        this.database = database;
        this.handler = handler;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("rtfm... usage: toggle [command]");
            return;
        }

        String command = args[0];

        // SpongyBot 1 support
        if (command.startsWith("#")) {
            request.reply("You no longer need to add a # at the start of the command name to disable it, support for this will be removed soon.");
            command = command.substring(1);
        }

        Command cmd = handler.getCommand(command);

        if (cmd == null) {
            request.reply("Invalid command '" + command + "'!");
            return;
        }

        String room = request.getRoom();
        RoomData data = database.getData(room);
        RoomSettings settings = data.getSettings();

        @SuppressWarnings("unchecked")
        List<String> disabledCommands = (List<String>) settings.get(Setting.DISABLED_COMMANDS);
        String reply;

        String firstName = cmd.getNames()[0].toLowerCase();
        if (noDisable.contains(firstName)) {
            request.reply("That command cannot be disabled!");
            return;
        }

        if (!disabledCommands.contains(firstName)) {
            disabledCommands.add(firstName);
            reply = "Disabled the command!";
        } else {
            disabledCommands.remove(firstName);
            reply = "Enabled the command!";
        }
        settings.set(Setting.DISABLED_COMMANDS, disabledCommands);
        database.save(room);

        request.reply(reply);
    }
}
