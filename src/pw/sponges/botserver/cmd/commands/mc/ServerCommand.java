package pw.sponges.botserver.cmd.commands.mc;

import org.json.JSONObject;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.permissions.simple.UserRole;
import pw.sponges.botserver.util.FileUtils;
import pw.sponges.botserver.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

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

        URL url = null;
        try {
            url = new URL("https://mcapi.ca/query/" + ip + ":" + port + "/info");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String json = FileUtils.readUrl(url);
        if (json == null) {
            request.reply("Could not get the status of that server!");
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
