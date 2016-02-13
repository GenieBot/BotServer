package io.sponges.bot.server.parser.parsers;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import io.sponges.bot.server.parser.framework.Parser;

public class TwitchParser extends Parser {

    public TwitchParser() {
        super("twitch.tv/");
    }

    @Override
    public String parse(String content) {
        Channel channel;
        try {
            channel = new Channel(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (channel.isLive()) {
            return channel.getName() + ": \"" + channel.getTitle() + "\" playing " + channel.getGame() + "!"
                    + String.format(" (%s viewers, %s total views, %s followers)", channel.getWatchers(),
                    channel.getTotalViews(), channel.getFollowers());
        } else {
            return String.format("%s - %s total views - %s followers", channel.getName(), channel.getTotalViews(), channel.getFollowers());
        }
    }

} class Channel {

    private final String username, name, title, game, team, language;
    private final boolean live, partner, mature;
    private final int id, watchers, totalViews, followers;

    public Channel(String url) throws Exception {
        String username = url.substring(url.indexOf(".tv/") + 4, url.length());
        if (username.contains("?")) {
            username = username.substring(0, username.indexOf("?"));
        }

        // first api call
        String channelsResponse = Jsoup.connect("https://api.twitch.tv/kraken/channels/" + username)
                .ignoreContentType(true).userAgent("mozilla/5.0").execute().body();
        JSONObject json = new JSONObject(channelsResponse);

        // second api call
        String streamResponse = Jsoup.connect("http://streams.twitch.tv/kraken/streams/" + username + "?stream_type=all&on_site=1")
                .ignoreContentType(true).userAgent("mozilla/5.0").execute().body();
        JSONObject stream = new JSONObject(streamResponse);

        this.username = username;
        this.name = json.getString("display_name");
        this.title = json.getString("status");
        this.game = json.getString("game");

        this.live = !stream.isNull("stream");

        if (live) this.watchers = stream.getJSONObject("stream").getInt("viewers");
        else this.watchers = 0;

        this.team = null;
        this.language = json.getString("language");
        this.partner = json.getBoolean("partner");
        this.mature = json.getBoolean("mature");
        this.id = json.getInt("_id");
        this.totalViews = json.getInt("views");
        this.followers = json.getInt("followers");
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getGame() {
        return game;
    }

    public String getTeam() {
        return team;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isLive() {
        return live;
    }

    public boolean isPartner() {
        return partner;
    }

    public boolean isMature() {
        return mature;
    }

    public int getId() {
        return id;
    }

    public int getWatchers() {
        return watchers;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public int getFollowers() {
        return followers;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("username", username)
                .put("name", name)
                .put("title", title)
                .put("game", game)
                .put("team", team)
                .put("language", language)
                .put("live", live)
                .put("partner", partner)
                .put("mature", mature)
                .put("id", id)
                .put("watchers", watchers)
                .put("totalviews", totalViews)
                .put("followers", followers)
                .toString();
    }
}