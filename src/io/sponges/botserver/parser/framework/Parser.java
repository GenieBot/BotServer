package io.sponges.botserver.parser.framework;

public abstract class Parser {

    private final String[] urls;

    public Parser(String... urls) {
        this.urls = urls;
    }

    public abstract String parse(String content);

    public String[] getUrls() {
        return urls;
    }
}
