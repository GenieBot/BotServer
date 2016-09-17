package io.sponges.bot.server.protocol.manager;

import io.sponges.bot.api.entities.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ResourceRequestManager<T extends Entity> {

    private final Map<String, Consumer<T>> requests = new ConcurrentHashMap<>();

    public Map<String, Consumer<T>> getRequests() {
        return requests;
    }

}
