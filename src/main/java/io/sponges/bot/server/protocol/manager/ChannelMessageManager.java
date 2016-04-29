package io.sponges.bot.server.protocol.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ChannelMessageManager {

    private final Map<String, Consumer<String>> messages = new ConcurrentHashMap<>();

    public Map<String, Consumer<String>> getMessages() {
        return messages;
    }

}
