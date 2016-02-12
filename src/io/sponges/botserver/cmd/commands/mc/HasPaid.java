package io.sponges.botserver.cmd.commands.mc;

import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class HasPaid extends Command {

    public final String CONNECTION_URL = "https://mcapi.ca/other/haspaid/%s";

    public HasPaid() {
        super("command.haspaid", UserRole.USER, "checks if a minecraft username is premium", "haspaid", "hp", "mcaccount", "mcchecker", "ispaid", "ispremium", "haspremium");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("u must be drunk! usage: haspaid [username]");
            return;
        }

        String url = String.format(CONNECTION_URL, args[0]);
        String result;

        try {
            result = Jsoup.connect(url).ignoreContentType(true).header("X-Request-By-Username", request.getUser().getId()).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            request.reply("Something went wrong whilst connecting to mcapi.ca! Is it online?");
            return;
        }

        JSONObject json = new JSONObject(result);
        boolean premium = json.getBoolean("premium");

        if (premium) {
            request.reply("That username is premium :(");
        } else {
            request.reply("That username is not premium :)");
        }
    }
}
