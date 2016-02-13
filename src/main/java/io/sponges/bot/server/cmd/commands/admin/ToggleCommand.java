package io.sponges.bot.server.cmd.commands.admin;

import io.sponges.bot.server.cmd.framework.CommandHandler;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;

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

        Room room = request.getRoom();
        String roomId = room.getId();
        RoomData data = room.getRoomData();

        @SuppressWarnings("unchecked")
        List<String> disabledCommands = (List<String>) data.get(Setting.DISABLED_COMMANDS);
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

        data.set(Setting.DISABLED_COMMANDS, disabledCommands);
        database.save(room);

        request.reply(reply);
    }
}
