package io.sponges.bot.server.protocol.manager;

import io.sponges.bot.api.entities.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ResourceRequestManager {

    private final Map<String, Consumer<Entity>> requests = new ConcurrentHashMap<>();

    public Map<String, Consumer<Entity>> getRequests() {
        return requests;
    }

}
