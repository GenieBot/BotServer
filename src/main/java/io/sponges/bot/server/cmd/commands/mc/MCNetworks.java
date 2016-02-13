package io.sponges.bot.server.cmd.commands.mc;

import io.sponges.bot.server.cmd.framework.Command;
import io.sponges.bot.server.cmd.framework.CommandRequest;
import io.sponges.bot.server.storage.UserRole;
import org.json.JSONArray;
import org.json.JSONObject;
import io.sponges.bot.server.util.FileUtils;
import io.sponges.bot.server.util.Scheduler;

import java.net.MalformedURLException;
import java.net.URL;

public class MCNetworks extends Command {

    private String cached = null;
    private long expire = 0;

    public MCNetworks() {
        super("command.mcnetworks", UserRole.USER, "player counts of the biggest mc servers", "mcnetworks", "mcnetwork", "minecraftnetworks", "hypixel", "mineplex", "bigservers", "mcservers", "network", "networks");

        //Scheduler.runAsyncTask(this::reload, 30, TimeUnit.SECONDS);
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        /*if (expire == 0 || cached == null || System.currentTimeMillis() >= expire) {
            System.out.println("reloading");
            reload();
        }

        if (cached == null) {
            request.reply("Network stats are still loading, try again in a sec.");
            return;
        }

        request.reply(cached.replace("%update%", ((expire - System.currentTimeMillis()) / 1000) + "s"));*/

        request.reply("The Minetrack API has been depreciated, however it is being redone soon! Check out the site: http://minetrack.me/");
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
            json = FileUtils.readUrl(new URL("http://minetrack.me/status.json"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        return new JSONObject(json);
    }
}
