package io.sponges.botserver.parser.framework;

import io.sponges.botserver.parser.parsers.TestParser;
import io.sponges.botserver.parser.parsers.TwitchParser;
import io.sponges.botserver.parser.parsers.YoutubeParser;
import io.sponges.botserver.parser.parsers.TwitterParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParserManager {

    private final List<Parser> parsers = new ArrayList<>();

    public ParserManager() {
        this.register(
                new TestParser(),
                new TwitterParser(),
                new YoutubeParser(),
                new TwitchParser()
        );
    }

    private void register(Parser... parsers) {
        Collections.addAll(this.parsers, parsers);
    }

    public String handle(String link) {
        String lowercase = link.toLowerCase();

        for (Parser parser : parsers) {
            for (String s : parser.getUrls()) {
                if (lowercase.contains(s)) {
                    return parser.parse(link);
                }
            }
        }

        return null;
    }

}
