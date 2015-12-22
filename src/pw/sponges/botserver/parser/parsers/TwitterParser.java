package pw.sponges.botserver.parser.parsers;

import pw.sponges.botserver.parser.framework.Parser;

import java.util.Arrays;

public class TwitterParser extends Parser {

    public TwitterParser() {
        super("twitter.com/");
    }

    @Override
    public String parse(String content) {
        return "Fired test parser for content " + content + " checking for " + Arrays.toString(getUrls());
    }
}
