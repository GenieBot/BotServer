package io.sponges.bot.server.parser.parsers;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import io.sponges.bot.server.parser.framework.Parser;

import java.io.IOException;

public class YoutubeParser extends Parser {

    public YoutubeParser() {
        super("youtube.com/watch?v=");
    }

    @Override
    public String parse(String content) {
        String splitter = "/watch?v=";
        int splitterLength = splitter.length();
        String code = content.substring(content.indexOf(splitter) + splitterLength);

        if (code.contains("&")) {
            int indexOf = code.indexOf("&");
            code = new StringBuilder(code).delete(indexOf, code.length()).toString();
        }

        Video video = new Video(code);
        if (!video.load()) return null;

        return String.format("%s (+%s -%s)\n%s - [%s]", video.getTitle(), video.getLikes(), video.getDislikes(), StringUtils.abbreviate(video.getDesc(), 100), video.getShortUrl());
    }

    // TODO caching

    public static class Video {

        private final String id;
        private Element body;

        public Video(String id) {
            this.id = id;
        }

        public boolean load() {
            try {
                body = Jsoup.connect("https://www.youtube.com/watch?v=" + id).get().body();
            } catch (IOException e) {
                return false;
            }

            return true;
        }

        public String dumpData() {
            return ("Title: " + getTitle()) + "Preview: " + getPreview()
                    + "\n" + "Views: " + getViews()
                    + "\n" + "ShortUrl: " + getShortUrl()
                    + "\n" + "Dislikes: " + getDislikes()
                    + "\n" + "Likes: " + getLikes()
                    + "\n" + "Uploader: " + getUploader()
                    + "\n" + getDesc();
        }


        public String getTitle() {
            return body.getElementById("eow-title").text();
        }

        public String getPreview() {
            return getWatchMeta("thumbnailUrl");
        }

        public int getViews() {
            return Integer.parseInt(getClassText("watch-view-count").replace(",", ""));
        }

        public String getShortUrl() {
            return "http://youtu.be/" + id;
        }

        public int getDislikes() {
            return Integer.parseInt(getClassText("like-button-renderer-dislike-button"));
        }

        public int getLikes() {
            return Integer.parseInt(getClassText("like-button-renderer-dislike-button"));
        }

        public String getDesc() {
            return body.getElementById("eow-description").text();
        }

        public String getUploader() {
            return getWatchMeta("author");
        }

        private String getClassText(String cls) {
            return body.getElementsByClass(cls).get(0).text();
        }

        private String getWatchMeta(String id) {
            Elements elements = body.getElementById("watch7-content").getAllElements();
            for (Element element : elements) {
                if (element.hasAttr("itemprop"))
                    if (element.attr("itemprop").equals(id)) {
                        if (element.hasAttr("content")) {
                            return element.attr("content");
                        } else {
                            for (Element subElement : elements) {
                                if (subElement.hasAttr("content"))
                                    return subElement.attr("content");
                                if (subElement.hasAttr("href"))
                                    return subElement.attr("href");
                            }
                        }
                    }
            }

            return null;
        }
    }
}
