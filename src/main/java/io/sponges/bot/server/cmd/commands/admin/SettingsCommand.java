package io.sponges.bot.server.cmd.commands.admin;

import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.storage.Database;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.util.ParsingUtils;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.cmd.framework.Command;

public class SettingsCommand extends Command {

    private Database database;

    public SettingsCommand(Database database) {
        super("command.settings", UserRole.ADMIN, "room setting management", "settings", "set", "admin");
        this.database = database;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        Room room = request.getRoom();
        RoomData roomData = room.getRoomData();

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload": {
                    database.load(room);
                    request.reply("Reloaded " + room + " data!");
                    break;
                }

                default: {
                    String setting = args[0].toUpperCase().replace("-", "_");
                    Setting set = null;
                    Object value = null;

                    for (Setting s : Setting.values()) {
                        if (s.name().equals(setting)) {
                            set = s;
                            value = roomData.get(s);
                            break;
                        }
                    }

                    if (set == null) {
                        request.reply("Invalid setting " + setting + "!");
                        break;
                    }

                    if (set == Setting.PREFIX) {
                        request.reply("Please use the 'prefix' command instead!");
                        return;
                    }

                    if (args.length == 1) {
                        request.reply(setting + ": " + value.toString() + "\nSet the value with "
                                + roomData.get(Setting.PREFIX) + "settings "
                                + setting + " [value]");
                        break;
                    } else if (args.length == 2) {
                        Object v = ParsingUtils.parse(args[1]);
                        roomData.set(set, v);
                        database.save(room);
                        request.reply("Set the value of " + setting.toLowerCase().replace("_", "-") + " to " + v + "!");
                        break;
                    } else {
                        request.reply("Invalid arguments!");
                        break;
                    }
                }
            }

            return;
        }

        StringBuilder str = new StringBuilder("Current room settings:");

        for (Setting s : Setting.values()) {
            str.append("\n").append(s.toString()).append(": ").append(roomData.get(s));
        }

        if ((boolean) roomData.get(Setting.SHOW_FULL_JSON)) str.append("\n").append(roomData.toJson().toString());

        request.reply(str.toString());
    }

}
