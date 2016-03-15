package io.sponges.bot.server.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class Configuration {

    public static final String CONFIG_DEFAULTS = new JSONObject()
            .put("server", new JSONObject()
                            .put("port", 9574)
            ).put("redis", new JSONObject()
                            .put("host", "localhost")
                            .put("port", 6379)
            ).toString();

    public JSONObject load(File file) throws IOException, JSONException {
        if (!file.exists()) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(CONFIG_DEFAULTS).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            write(file, gson.toJson(json));
            return null;
        }

        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String input;
            while ((input = reader.readLine()) != null) {
                builder.append(input).append("\n");
            }
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new JSONObject(builder.toString());
    }

    private void write(File file, String contents) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(contents);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
