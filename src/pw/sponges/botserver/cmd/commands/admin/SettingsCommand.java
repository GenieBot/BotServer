package pw.sponges.botserver.cmd.commands.admin;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.Setting;

public class SettingsCommand extends Command {

    private Database database;

    public SettingsCommand(Database database) {
        super("command.settings", "settings", "set", "admin");
        this.database = database;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        String room = request.getRoom();

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                database.load(room);
                request.reply("Reloaded " + room + " data!");
                return;
            } else {
                request.reply("Invalid arguments!");
                return;
            }
        }

        RoomData roomData = database.getData(room);
        StringBuilder str = new StringBuilder("Current room settings:");

        for (Setting s : Setting.values()) {
            str.append("\n").append(s.toString()).append(": ").append(roomData.getSettings().get(s));
        }

        str.append("\n").append(roomData.toJson().toString());

        request.reply(str.toString());
    }

}
