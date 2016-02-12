package io.sponges.botserver.cmd.commands.admin;

import io.sponges.botserver.Client;
import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.framework.Room;
import io.sponges.botserver.messages.KickUserMessage;
import io.sponges.botserver.storage.UserRole;
import io.sponges.botserver.storage.Database;
import io.sponges.botserver.storage.RoomData;
import io.sponges.botserver.storage.Setting;

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

        RoomData data = room.getRoomData();

        @SuppressWarnings("unchecked")
        List<String> bannedUsers = (List<String>) data.get(Setting.BANNED_USERS);

        String reply;
        if (!bannedUsers.contains(user)) {
            bannedUsers.add(user);
            client.sendMessage(new KickUserMessage(client, room, args[0]));
            reply = String.format("Banned %s! To unban them, use the 'unban' command.", user);
        } else {
            bannedUsers.remove(user);
            reply = String.format("Unbanned %s!", user);
        }

        data.set(Setting.BANNED_USERS, bannedUsers);
        database.save(room);

        request.reply(reply);
    }

}
