package io.sponges.bot.server.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class StrawpollUtils {

    public static StrawpollResults getResults(int id) throws IOException {
        String response = Jsoup.connect("https://strawpoll.me/api/v2/polls/" + id)
                .header("User-Agent", "Mozilla/5.0")
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute().body();

        JSONObject json = new JSONObject(response);
        String title = json.getString("title");
        boolean multi = json.getBoolean("multi");

        JSONArray keysArray = json.getJSONArray("options");
        String[] keys = new String[keysArray.length()];

        for (int i = 0; i < keysArray.length(); i++) {
            keys[i] = keysArray.getString(i);
        }

        JSONArray valuesArray = json.getJSONArray("votes");
        int[] values = new int[keysArray.length()];

        for (int i = 0; i < keysArray.length(); i++) {
            values[i] = valuesArray.getInt(i);
        }

        return new StrawpollResults(id, title, multi, keys, values);
    }

} class StrawpollResults {

    private final int id;
    private final String title;
    private final boolean multi;
    private final Map<String, Integer> votes;

    public StrawpollResults(int id, String title, boolean multi, String[] keys, int[] values) {
        this.id = id;
        this.title = title;
        this.multi = multi;

        this.votes = new HashMap<>();

        for (int i = 0; i < keys.length; i++) {
            this.votes.put(keys[i], values[i]);
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isMulti() {
        return multi;
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    public int getVotes(String key) {
        return votes.get(key);
    }
}
