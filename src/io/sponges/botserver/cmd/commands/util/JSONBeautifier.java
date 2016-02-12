package io.sponges.botserver.cmd.commands.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.sponges.botserver.cmd.framework.Command;
import io.sponges.botserver.cmd.framework.CommandRequest;
import io.sponges.botserver.storage.UserRole;
import org.apache.commons.lang3.StringEscapeUtils;

public class JSONBeautifier extends Command {

    public JSONBeautifier() {
        super("command.jsonbeautifier", UserRole.USER, "beautifies a json string", "jsonbeautifier", "prettify", "beautify", "jsonprint", "jsoncleaner", "prettyjson", "hotjson");
    }

    @Override
    public void onCommand(CommandRequest request, String[] args) {
        if (args.length == 0) {
            request.reply("Usage: beautify [json string]");
            return;
        }

        StringBuilder str = new StringBuilder();

        for (String s : args) {
            str.append(StringEscapeUtils.unescapeJson(s)).append(" ");
        }

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(str.toString()).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        request.reply(gson.toJson(json));
    }
}
