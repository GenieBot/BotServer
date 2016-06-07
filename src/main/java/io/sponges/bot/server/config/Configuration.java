package io.sponges.bot.server.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class Configuration {

    private static final String CONFIG_DEFAULTS = new JSONObject()
            .put("server", new JSONObject()
                            .put("port", 9574)
            ).put("redis", new JSONObject()
                            .put("host", "localhost")
                            .put("port", 6379)
            ).toString();

    public JSONObject load(File file) throws IOException, JSONException {
        if (!file.exists()) {
            write(file, new JSONObject(CONFIG_DEFAULTS).toString(4));
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
