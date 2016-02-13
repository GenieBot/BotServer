package io.sponges.bot.server.parser.parsers;

import io.sponges.bot.server.parser.framework.Parser;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class TwitterParser extends Parser {

    public TwitterParser() {
        super("twitter.com/");
    }

    @Override
    public String parse(String content) {
        if (!content.contains("/status/")) return null;

        Tweet tweet;
        try {
            tweet = new Tweet(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String message = tweet.getContent();
        String indexer = "pic.twitter.com";
        int startPoint = message.indexOf(indexer);
        StringBuilder str = new StringBuilder(message);

        if (message.contains(indexer)) {
            str.insert(startPoint, "[http://");
            str.insert(str.length(), "]");
        }

        return String.format("%s (%s): %s", tweet.getName(), tweet.getUsername(), str.toString());
    }

} class Tweet {

    private final String content, username, name;

    public Tweet(String tweet) throws IOException {
        Document document = Jsoup.connect(tweet).header("User-Agent", "mozilla/5.0").get();
        Element userActions = document.getElementsByClass("follow-bar").get(0).getElementsByClass("user-actions").get(0);

        content = document.getElementsByClass("TweetTextSize--28px").get(0).text();;
        username = userActions.attr("data-screen-name");;
        name = userActions.attr("data-name");;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("content",content)
                .put("username", username)
                .put("name", name)
                .toString();
    }
}
