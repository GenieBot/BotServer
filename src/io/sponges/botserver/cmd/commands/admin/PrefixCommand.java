package io.sponges.botserver.cmd.commands.admin;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.framework.Room;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.storage.Database;
import io.sponges.botserver.storage.Setting;

public class PrefixCommand extends Command {

    private Database database;

    public PrefixCommand(Database database) {
        super("command.prefix", UserRole.ADMIN, "changes the prefix for commands", "prefix");
        this.database = database;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        switch (request.getClient().getId().toLowerCase()) {
            case "telegram": {
                request.reply("Changing command prefix is not supported in telegram!");
                return;
            }
        }

        if (args.length == 0) {
            request.reply("Invalid arguments! Usage: prefix <new prefix>");
            return;
        }

        String prefix = args[0];

        Room room = request.getRoom();
        room.getRoomData().set(Setting.PREFIX, prefix);
        database.save(request.getRoom());

        request.reply("Set the prefix for " + request.getRoom().getId() + " to " + args[0] + "!");
    }
}
