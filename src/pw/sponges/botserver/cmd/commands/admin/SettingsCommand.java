package pw.sponges.botserver.cmd.commands.admin;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.Setting;
import pw.sponges.botserver.util.ParsingUtils;

public class SettingsCommand extends Command {

    private Database database;

    public SettingsCommand(Database database) {
        super("command.settings", UserRole.ADMIN, "room setting management", "settings", "set", "admin");
        this.database = database;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        String room = request.getRoom().getId();
        RoomData roomData = database.getData(room);

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

                    for (Setting s : roomData.getSettings().getDefaults().keySet()) {
                        if (s.name().equals(setting)) {
                            set = s;
                            value = roomData.getSettings().get(s);
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

                    if (set == Setting.SIMPLE_PERMS) {
                        request.reply("Node based permissions are not yet supported!");
                        return;
                    }

                    if (args.length == 1) {
                        request.reply(setting + ": " + value.toString() + "\nSet the value with "
                                + roomData.getSettings().getSettings().get(Setting.PREFIX) + "settings "
                                + setting + " [value]");
                        break;
                    } else if (args.length == 2) {
                        Object v = ParsingUtils.parse(args[1]);
                        roomData.getSettings().set(set, v);
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
            str.append("\n").append(s.toString()).append(": ").append(roomData.getSettings().get(s));
        }

        if ((boolean) roomData.getSettings().get(Setting.SHOW_FULL_JSON)) str.append("\n").append(roomData.toJson().toString());

        request.reply(str.toString());
    }

}
