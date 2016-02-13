package io.sponges.bot.server.cmd.commands.mc;

import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;
import io.sponges.bot.server.util.StringUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ServerCommand extends Command {

    public ServerCommand() {
        super("command.mcserver", UserRole.USER, "shows ping info of an mc server", "mcserver", "s", "ping", "slp");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Invalid arguments! Usage: server <ip:port>");
            return;
        }

        String input = args[0];
        String ip = null;
        int port = 25565;

        if (input.contains(":")) {
            String[] split = input.split(":");
            ip = split[0];
            port = Integer.parseInt(split[1]);
        } else {
            ip = input;
        }

        if (ip == null) {
            request.reply("The IP you entered doesn't seem to be valid!");
            return;
        }

        String json;
        try {
            json = Jsoup.connect("http://mcapi.ca/query/" + ip + ":" + port + "/info").timeout(1000).userAgent("SpongyBot").header("X-Request-From-User", request.getUser().getId()).header("X-Request-From-Platform", request.getClient().getId()).method(Connection.Method.GET).execute().body();
        } catch (IOException e) {
            request.reply("Could not get the status of that server! Is mcapi.ca down?");
            return;
        }

        if (json == null) {
            request.reply("Could not get the status of that server! Is mcapi.ca down?");
            return;
        }

        JSONObject object = new JSONObject(json);

        if (!object.getBoolean("status")) {
            request.reply("Could not query that server!");
            return;
        }

        JSONObject players = object.getJSONObject("players");
        StringBuilder str = new StringBuilder();

        str.append("Status of ").append(ip).append(":").append(port).append("\n");
        str.append("Players: ").append(players.getInt("online")).append("/").append(players.getInt("max"));
        str.append("\n").append("MOTD: ").append(StringUtils.escape(object.getString("motd")));

        request.reply(str.toString());
    }
}
