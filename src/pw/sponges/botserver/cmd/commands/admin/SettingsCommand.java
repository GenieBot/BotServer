package pw.sponges.botserver.cmd.commands.admin;

import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.messages.SettingUpdateMessage;
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
        RoomData roomData = database.getData(room);

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload": {
                    database.load(room);
                    request.reply("Reloaded " + room + " data!");
                    break;
                }

                default: {
                    String setting = args[0];
                    Setting set = null;
                    Object value = null;

                    for (Setting s : roomData.getSettings().getDefaults().keySet()) {
                        if (s.name().equalsIgnoreCase(setting)) {
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
                        switch (request.getClient().getId().toLowerCase()) {
                            case "telegram": {
                                request.reply("Changing command prefix is not supported in telegram!");
                                return;
                            }
                        }
                    }

                    if (args.length == 1) {
                        request.reply(setting + ": " + value.toString() + "\nSet the value with "
                                + roomData.getSettings().getSettings().get(Setting.PREFIX) + "settings "
                                + setting + " [value]");
                        break;
                    } else if (args.length == 2) {
                        Object v = args[1];
                        roomData.getSettings().set(set, v);
                        database.save(room);
                        request.getClient().sendMessage(new SettingUpdateMessage(request.getClient(), room, set, v));
                        request.reply("Set the value of " + setting + " to " + v + "!");
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

        str.append("\n").append(roomData.toJson().toString());

        request.reply(str.toString());
    }

}
