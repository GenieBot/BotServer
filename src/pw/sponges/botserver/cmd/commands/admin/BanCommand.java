package pw.sponges.botserver.cmd.commands.admin;

import pw.sponges.botserver.Client;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.framework.Room;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.storage.Database;
import pw.sponges.botserver.storage.RoomData;
import pw.sponges.botserver.storage.RoomSettings;
import pw.sponges.botserver.storage.Setting;

import java.util.List;

public class BanCommand extends Command {

    private final Database database;

    public BanCommand(Database database) {
        super("command.ban", UserRole.ADMIN, "bans a user from the chat", "ban", "unban", "kickban", "bankick", "banuser", "banmember", "pardon");

        this.database = database;
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Do you have an IQ? Usage: ban/unban [user id/username]");
            return;
        }

        Client client = request.getClient();
        Room room = request.getRoom();
        String user = args[0].toLowerCase();

        RoomData data = database.getData(room.getId());
        RoomSettings settings = data.getSettings();

        @SuppressWarnings("unchecked")
        List<String> bannedUsers = (List<String>) settings.get(Setting.BANNED_USERS);

        String reply;
        if (!bannedUsers.contains(user)) {
            bannedUsers.add(user);
            // TODO fix ban command by adding username alternative kick constructor
            //client.sendMessage(new KickUserMessage(client, room, args[0]));
            reply = String.format("Banned %s! To unban them, use the 'unban' command.", user);
        } else {
            bannedUsers.remove(user);
            reply = String.format("Unbanned %s!", user);
        }

        settings.set(Setting.BANNED_USERS, bannedUsers);
        database.save(room.getId());

        request.reply(reply);
    }

}
