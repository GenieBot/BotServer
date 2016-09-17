package io.sponges.bot.server.protocol.parser.initalizer;

import java.util.UUID;

public abstract class Initializer {

    protected static UUID generateNewUUID() {
        return UUID.randomUUID();
    }

}
