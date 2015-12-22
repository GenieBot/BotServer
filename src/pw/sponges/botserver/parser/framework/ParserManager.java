package pw.sponges.botserver.parser.framework;

import pw.sponges.botserver.parser.parsers.TestParser;

import java.util.ArrayList;
import java.util.List;

public class ParserManager {

    private final List<Parser> parsers = new ArrayList<>();

    public ParserManager() {
        this.register(
                new TestParser()
        );
    }

    private void register(Parser... parsers) {
        for (Parser parser : parsers) {
            this.parsers.add(parser);
        }
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
