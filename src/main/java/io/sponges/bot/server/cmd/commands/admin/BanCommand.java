package io.sponges.bot.server.cmd.commands.admin;

import io.sponges.bot.server.framework.Room;
import io.sponges.bot.server.storage.RoomData;
import io.sponges.bot.server.storage.Setting;
import io.sponges.bot.server.Client;
import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.messages.KickUserMessage;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.storage.Database;

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
