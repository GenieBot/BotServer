package io.sponges.bot.server.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class Configuration {

    // this is ugly as shit
    private static final JSONObject CONFIG_DEFAULTS = new JSONObject()
            .put("server", new JSONObject()
                            .put("port", 9574)
            ).put("redis", new JSONObject()
                            .put("host", "localhost")
                            .put("port", 6379)
            ).put("webhook-server", new JSONObject()
                            .put("port", 4598)
            );

    public JSONObject load(File file) throws IOException, JSONException {
        if (!file.exists()) {
            write(file, CONFIG_DEFAULTS.toString(4));
            return null;
        }
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String input;
            while ((input = reader.readLine()) != null) {
                builder.append(input).append("\n");
            }
        }
        return new JSONObject(builder.toString());
    }

    private void write(File file, String contents) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(contents);
        }
    }

}
