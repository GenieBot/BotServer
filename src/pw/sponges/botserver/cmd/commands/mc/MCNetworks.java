package pw.sponges.botserver.cmd.commands.mc;

import org.json.JSONArray;
import org.json.JSONObject;
import pw.sponges.botserver.cmd.framework.Command;
import pw.sponges.botserver.cmd.framework.CommandRequest;
import pw.sponges.botserver.util.FileUtils;
import pw.sponges.botserver.util.Scheduler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MCNetworks extends Command {

    private String cached = null;
    private long expire = 0;

    public MCNetworks() {
        super("command.mcnetworks", "mcnetworks", "minecraftnetworks", "hypixel", "mineplex", "bigservers", "mcservers", "network", "networks");

        Scheduler.runAsyncTask(this::reload, 30, TimeUnit.SECONDS);
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (expire == 0 || cached == null || System.currentTimeMillis() >= expire) {
            System.out.println("reloading");
            reload();
        }

        System.out.println("printing " + cached);

        if (cached == null) {
            request.reply("Network stats are still loading, try again in a sec.");
            return;
        }

        request.reply(cached.replace("%update%", ((expire - System.currentTimeMillis()) / 1000) + "s"));
        System.out.println("printed");
    }

    private void reload() {
        // Update every 30s
        expire = System.currentTimeMillis() + 30000;

        Scheduler.runAsyncTask(() -> {
            StringBuilder builder = new StringBuilder("Player counts for the big boys:");
            JSONObject json = getStats();

            if (json == null) {
                cached = "Request did not work :(";
                return;
            }

            JSONArray array = json.getJSONArray("networks");

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                String status = object.getString("status");
                builder.append("\n").append(object.getString("ip")).append(": ");

                if (status.equals("ONLINE")) {
                    builder.append(object.getInt("onlinePlayers")).append("/").append(object.getInt("maxPlayers"));
                } else {
                    builder.append("offline");
                }
            }

            builder.append("\nNext update: %update%");
            cached = builder.toString();
        });
    }

    private JSONObject getStats() {
        String json;

        try {
            json = FileUtils.readUrl(true, new URL("http://minetrack.me/status.json"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        return new JSONObject(json);
    }
}
